package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;
import java.util.Optional;

public interface IssuedRepositoryCustom {

    List<IssuedResponse> findCouponByUser(User user);

    Optional<Double> getDiscountFindById(Long userId,Long issueId);

    void setDeletedFindById(Long issueId);

    Coupon findByIssued(Long issueId);

    List<Issued> findByCouponIdAndUser(Long couponId, User user);

}