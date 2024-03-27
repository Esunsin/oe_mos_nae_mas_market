package cheolppochwippo.oe_mos_nae_mas_market.user.controller;

import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.global.security.JwtUtil;
import cheolppochwippo.oe_mos_nae_mas_market.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.user.dto.UserResponse;
import cheolppochwippo.oe_mos_nae_mas_market.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/signup")
    public ResponseEntity<CommonResponse<UserResponse>> signup(@RequestBody UserRequest userRequest) {
        UserResponse signupedUser = userService.signup(userRequest);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<UserResponse>builder()
                        .msg("signup complete!")
                        .data(signupedUser)
                        .build());
    }

    @PostMapping("/auth/login")
    public ResponseEntity<CommonResponse<UserResponse>> login(@RequestBody UserRequest userRequest, HttpServletResponse response) {
        UserResponse loginedUser = userService.login(userRequest);
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(userRequest.getUsername()));
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<UserResponse>builder()
                        .msg("login complete!")
                        .data(loginedUser)
                        .build());
    }
}
