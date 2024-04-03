package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;
import java.util.Optional;

public interface IssuedRepositoryCustom {

    List<Issued> findByCouponIdAndUser(Long couponId, User user);

    List<IssuedResponse> findCouponByUser(User user);

    Optional<Double> getDiscountFindById(Long userId,Long issueId);

    void setDeletedFindById(Long issueId);

}