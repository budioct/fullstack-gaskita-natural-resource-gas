package budhioct.dev.rest.controller;

import budhioct.dev.rest.config.RestResponse;
import budhioct.dev.security.jwt.LogoutService;
import budhioct.dev.security.module.UserDTO;
import budhioct.dev.security.module.UserService;
import budhioct.dev.utilities.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserRestController {

    private final UserService userService;
    private final LogoutService logoutService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RestResponse.object<UserDTO.RegisterResponse>> register(@RequestBody UserDTO.RegisterRequest request) {
        UserDTO.RegisterResponse userResponse = userService.register(request);
        RestResponse.object<UserDTO.RegisterResponse> res = RestResponse.object.<UserDTO.RegisterResponse>builder()
                .data(userResponse)
                .status_code(Constants.CREATED)
                .message(Constants.REGISTER_MESSAGE)
                .build();
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public RestResponse.object<UserDTO.LoginResponse> login(@RequestBody UserDTO.LoginRequest request) {
        UserDTO.LoginResponse loginResponse = userService.login(request);
        return RestResponse.object.<UserDTO.LoginResponse>builder()
                .data(loginResponse)
                .status_code(Constants.OK)
                .message(Constants.AUTH_LOGIN_MESSAGE)
                .build();
    }

    @PostMapping(
            path = "/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RestResponse.object<String> logout(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return RestResponse.object.<String>builder()
                .status_code(Constants.OK)
                .message(Constants.AUTH_LOGOUT_MESSAGE)
                .build();
    }
}
