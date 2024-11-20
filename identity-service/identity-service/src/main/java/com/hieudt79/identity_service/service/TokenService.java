package com.hieudt79.identity_service.service;

import com.hieudt79.identity_service.exception.InvalidDataException;
import com.hieudt79.identity_service.exception.ResourceNotFoundException;
import com.hieudt79.identity_service.model.Token;
import com.hieudt79.identity_service.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token getByUsername(String username) {
        return tokenRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("Not found token"));
    }

    public long save(Token token) {
        Optional<Token> optional = tokenRepository.findByEmail(token.getEmail());
        if (optional.isEmpty()) {
            tokenRepository.save(token);
            return token.getId();
        } else {
            Token t = optional.get();
            t.setAccessToken(token.getAccessToken());
            t.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(t);
            return t.getId();
        }
    }

    public void delete(String username) {
        Token token = getByUsername(username);
        tokenRepository.delete(token);
    }

    public boolean isExists(long id) {
        if (!tokenRepository.existsById(id)) {
            throw new InvalidDataException("Token not exists");
        }
        return true;
    }
}
