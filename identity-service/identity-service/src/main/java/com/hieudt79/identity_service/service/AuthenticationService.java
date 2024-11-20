package com.hieudt79.identity_service.service;

import com.hieudt79.identity_service.client.feign.dto.UserRegisterDTO;
import com.hieudt79.identity_service.dto.req.RegisterRequest;
import com.hieudt79.identity_service.dto.req.ResetPasswordDTO;
import com.hieudt79.identity_service.dto.req.SignInRequest;
import com.hieudt79.identity_service.dto.res.TokenResponse;
import com.hieudt79.identity_service.exception.InvalidDataException;
import com.hieudt79.identity_service.model.Role;
import com.hieudt79.identity_service.model.Token;
import com.hieudt79.identity_service.model.UserAccount;
import com.hieudt79.identity_service.model.UserRole;
import com.hieudt79.identity_service.repository.RoleRepository;
import com.hieudt79.identity_service.repository.UserAccountRepository;
import com.hieudt79.identity_service.utils.enums.UserStatus;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.hieudt79.identity_service.utils.enums.TokenType.*;
import static org.springframework.http.HttpHeaders.REFERER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserAccountService userService;
    private final MailService mailService;
    private final JwtService jwtService;

    public UserRegisterDTO registerUser(RegisterRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = request.getEmail();
        if(email.isBlank() || !mailService.isExist(email)) {
            throw new InvalidDataException("Invalid email");
        }

        UserRegisterDTO user =  new UserRegisterDTO();
        user.setFullName(request.getFullName());
        user.setBod(request.getBod());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setStatus(request.getStatus());
        return user;
    }


    public TokenResponse accessToken(SignInRequest signInRequest) {

        UserAccount user = userService.getByEmail(signInRequest.getEmail());
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }
        //update last login
        userService.updateLastLogin(signInRequest);

        List<String> roles = userService.getAllRoleByEmail(user.getUserId());
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword(), authorities));

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.save(Token.builder().email(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .build();
    }

    public TokenResponse refreshToken(HttpServletRequest request) {
        log.info("---------- refreshToken ----------");

        final String refreshToken = request.getHeader(REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }
        final String userName = jwtService.extractUsername(refreshToken, REFRESH_TOKEN);
        var user = userService.getByEmail(userName);
        if (!jwtService.isValid(refreshToken, REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Not allow access with this token");
        }

        String accessToken = jwtService.generateToken(user);

        tokenService.save(Token.builder().email(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .build();
    }


    public String removeToken(HttpServletRequest request) {
        log.info("---------- removeToken ----------");

        //get token from referer
        final String token = request.getHeader(REFERER);
        if (StringUtils.isBlank(token)) {
            throw new InvalidDataException("Token must be not blank");
        }

        //get username from token
        final String userName = jwtService.extractUsername(token, REFRESH_TOKEN);

        //delete token by username
        tokenService.delete(userName);

        return "Removed!";
    }

    public String forgotPassword(String email) {
        log.info("---------- forgotPassword ----------");

        UserAccount user = userService.getByEmail(email);

        String resetToken = jwtService.generateResetToken(user);

        tokenService.save(Token.builder().email(user.getUsername()).resetToken(resetToken).build());

        try {
            mailService.sendConfirmLink(email, resetToken);
        } catch (Exception e) {
            log.error("Send email fail, errorMessage={}", e.getMessage());
            throw new InvalidDataException("Send email fail, please try again!");
        }

        return resetToken;
    }

    public String resetPassword(String secretKey) {
        log.info("---------- Reset Password ----------");

        var user = validateToken(secretKey);
        if (user == null) {
            throw new IllegalArgumentException("Invalid or expired secret key");
        }
        var token = tokenService.getByUsername(user.getUsername());
        if (token == null) {
            throw new IllegalStateException("No token found for user");
        }

        log.info("Password reset initiated for user: {}", user.getUsername());
        return "Password reset successful";
    }

    public String changePassword(ResetPasswordDTO request) {
        log.info("---------- changePassword ----------");

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("Passwords do not match");
        }

        //get user from token reset password dto
        UserAccount user = validateToken(request.getSecretKey());

        // update password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.save(user);
        return "Changed";
    }

    //check user status
    private UserAccount validateToken(String token) {
        var userName = jwtService.extractUsername(token, RESET_TOKEN);

        var user = userService.getByEmail(userName);
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }
        return user;
    }
}