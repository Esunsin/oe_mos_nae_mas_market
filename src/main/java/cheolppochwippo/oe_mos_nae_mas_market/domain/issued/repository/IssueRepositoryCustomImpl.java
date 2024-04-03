package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.QCoupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.QIssued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IssueRepositoryCustomImpl implements IssueRepositoryCustom{

	private final JpaConfig jpaConfig;

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

	private BooleanExpression userIdEq(Long userId) {
		return Objects.nonNull(userId) ? QIssued.issued.user.id.eq(userId) : null;
	}

	private BooleanExpression issueIdEq(Long issueId) {
		return Objects.nonNull(issueId) ? QIssued.issued.id.eq(issueId) : null;
	}
}
