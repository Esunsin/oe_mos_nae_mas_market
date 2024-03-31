package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponResponse;
import java.util.List;

public interface CouponService {

    CouponResponse createCoupon(CouponRequest couponRequest);

    List<CouponResponse> getCoupons();

    CouponResponse updateCoupon(Long couponId, CouponRequest couponRequest);

    void deleteCoupon(Long couponId);
}
