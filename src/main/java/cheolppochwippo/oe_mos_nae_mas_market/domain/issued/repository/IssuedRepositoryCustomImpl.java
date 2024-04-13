package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.QCoupon.coupon;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.QIssued.issued;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.QIssued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class IssuedRepositoryCustomImpl implements IssuedRepositoryCustom {

    private final JpaConfig jpaConfig;

    private final EntityManager entityManager;

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

    @Override
    public Optional<Double> getDiscountFindById(Long userId,Long issueId){
        Double discount = jpaConfig.jpaQueryFactory()
            .select(QIssued.issued.coupon.discount)
            .from(QIssued.issued)
            .where(
                userIdEq(userId),
                issueIdEq(issueId)
            )
            .fetchOne();
        return Optional.ofNullable(discount);
    }

    @Override
    @Transactional
    public void setDeletedFindById(Long issueId) {
        Long count = jpaConfig.jpaQueryFactory()
            .update(QIssued.issued)
            .set(QIssued.issued.deleted, Deleted.DELETE)
            .where(issueIdEq(issueId))
            .execute();
        entityManager.flush();
        entityManager.clear();
    }

    private BooleanExpression userIdEq(Long userId) {
        return Objects.nonNull(userId) ? QIssued.issued.user.id.eq(userId) : null;
    }

    private BooleanExpression issueIdEq(Long issueId) {
        return Objects.nonNull(issueId) ? QIssued.issued.id.eq(issueId) : null;
    }

}
