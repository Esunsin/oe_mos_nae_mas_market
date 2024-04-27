package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.QCoupon.coupon;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom {
    private final JpaConfig jpaConfig;
    private final EntityManager entityManager;

    public List<Coupon> findAllUndeletedCoupons() {
        return jpaConfig.jpaQueryFactory()
            .selectFrom(coupon)
            .where(coupon.deleted.eq(Deleted.UNDELETE))
            .fetch();
    }

}
