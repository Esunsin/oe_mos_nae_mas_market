package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.service.CouponService;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<CommonResponse<CouponResponse>> createCoupon(
        @RequestBody CouponRequest couponRequest
    ){
        CouponResponse createdCoupon = couponService.createCoupon(couponRequest);
        return ResponseEntity.status(HttpStatus.CREATED.value())
            .body(CommonResponse.<CouponResponse>builder()
                .msg("coupon created complete!")
                .data(createdCoupon)
                .build());
    }

    @GetMapping("/coupons")
    public ResponseEntity<CommonResponse<List<CouponResponse>>> getCoupons(){
        List<CouponResponse> couponResponses = couponService.getCoupons();
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<List<CouponResponse>>builder()
                .msg("coupon get complete!")
                .data(couponResponses)
                .build());
    }

    @PatchMapping("/coupons/{couponId}")
    public ResponseEntity<CommonResponse<CouponResponse>> updateCoupon(
        @PathVariable Long couponId,
        @RequestBody CouponRequest couponRequest
    ) {
        CouponResponse updatedCoupon = couponService.updateCoupon(couponId, couponRequest);
        return ResponseEntity.status(HttpStatus.CREATED.value())
            .body(CommonResponse.<CouponResponse>builder()
                .msg("coupon created complete!")
                .data(updatedCoupon)
                .build());
    }

    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<CommonResponse<String>> deleteCoupon(
        @PathVariable Long couponId
    ) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value())
            .body(CommonResponse.<String>builder()
                .msg("coupon deleted complete!")
                .build());
    }

}
