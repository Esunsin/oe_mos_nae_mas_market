package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import java.util.List;

public interface CouponRepositoryCustom {

    List<Coupon> findAllUndeletedCoupons();

}
