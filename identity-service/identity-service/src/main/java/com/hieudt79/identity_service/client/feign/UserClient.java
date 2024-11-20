package com.hieudt79.identity_service.client.feign;
//
import com.hieudt79.identity_service.client.feign.dto.UserRegisterDTO;
import com.hieudt79.identity_service.config.FeignConfig;
import com.hieudt79.identity_service.dto.req.RegisterRequest;
import com.hieudt79.identity_service.dto.res.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:9000", configuration = FeignConfig.class)
public interface UserClient {
    @PostMapping(value = "/user/register", consumes = "application/json")
    public ResponseData<UserRegisterDTO> registerUser(@RequestBody RegisterRequest request);

}
