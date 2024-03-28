package cheolppochwippo.oe_mos_nae_mas_market.domain.user.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.global.util.JwtUtil;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(loginedUser.getUserId(), loginedUser.getUsername()));
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<UserResponse>builder()
                        .msg("login complete!")
                        .data(loginedUser)
                        .build());
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userDetails.getUser().getUsername();
    }

}
