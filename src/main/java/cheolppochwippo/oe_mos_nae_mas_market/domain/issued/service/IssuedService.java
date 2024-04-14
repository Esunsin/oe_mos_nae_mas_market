package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;

public interface IssuedService {

    IssuedResponse issueCoupon(Long couponId, User user);

    List<IssuedResponse> getIssuedCoupons(User user);

    void decreaseCouponAmount(Long couponId, User user);
}
