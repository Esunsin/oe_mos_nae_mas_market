package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service.IssuedService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IssuedController {
    private final IssuedService issuedService;

    @PostMapping("/issued/{couponId}")
    public ResponseEntity<CommonResponse<IssuedResponse>> issueCoupon(
        @PathVariable Long couponId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        IssuedResponse issuedCoupon = issuedService.issueCoupon(couponId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED.value())
            .body(CommonResponse.<IssuedResponse>builder()
                .msg("issue coupon complete!")
                .data(issuedCoupon)
                .build());
    }

    @GetMapping("/issued")
    public ResponseEntity<CommonResponse<List<IssuedResponse>>> getIssuedCoupons(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<IssuedResponse> issuedResponses = issuedService.getIssuedCoupons(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<List<IssuedResponse>>builder()
                .msg("issued coupon get complete!")
                .data(issuedResponses)
                .build());
    }

}
