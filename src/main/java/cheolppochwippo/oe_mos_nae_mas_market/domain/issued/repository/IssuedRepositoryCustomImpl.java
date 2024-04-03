package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.QCoupon.coupon;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.QIssued.issued;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import com.querydsl.core.types.Projections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IssuedRepositoryCustomImpl implements IssuedRepositoryCustom {

    private final JpaConfig jpaConfig;

    @Override
    public List<Issued> findByCouponIdAndUser(Long couponId, User user) {
        return jpaConfig.jpaQueryFactory()
            .selectFrom(issued)
            .leftJoin(issued.coupon).fetchJoin()
            .where(issued.deleted.eq(Deleted.UNDELETE)
                .and(issued.user.eq(user))
                .and(issued.coupon.id.eq(couponId)))
            .fetch();
    }

    public List<IssuedResponse> findCouponByUser(User user) {
        return jpaConfig.jpaQueryFactory()
            .select(
                Projections.constructor(
                    IssuedResponse.class,
                    issued.coupon.id,
                    issued.coupon.couponInfo,
                    issued.createdAt,
                    issued.deleted
                )
            )
            .from(issued)
            .leftJoin(issued.coupon, coupon)
            .where(
                issued.user.eq(user)
                    .and(issued.deleted.eq(Deleted.UNDELETE))
            )
            .fetch();
    }

}
