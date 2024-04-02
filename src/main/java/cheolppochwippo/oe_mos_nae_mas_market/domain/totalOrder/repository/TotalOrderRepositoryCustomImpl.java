package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.QTotalOrder.totalOrder;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.QTotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TotalOrderRepositoryCustomImpl implements TotalOrderRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	private final JpaConfig jpaConfig;

	@Override
	public Optional<TotalOrder> findByUserTotal(Long userId) {
		TotalOrder query = jpaQueryFactory.select(QTotalOrder.totalOrder)
			.from(QTotalOrder.totalOrder)
			.where(
				userIdEq(userId)
			).fetchOne();

		return Optional.ofNullable(query);
	}

	@Override
	public Long getTotalPriceByUserId(Long userId) {
		Long query = jpaQueryFactory.select(QOrder.order.price.sum())
			.from(QOrder.order)
			.where(
				userIdEq(userId)
			)
			.fetchOne();
		return query;
	}

	private BooleanExpression userIdEq(Long userId) {
		return Objects.nonNull(userId) ? QOrder.order.user.id.eq(userId) : null;
	}

	@Override
	public Optional<TotalOrder> findTotalOrderByUndeleted(User user){
		TotalOrder result = jpaConfig.jpaQueryFactory()
			.selectFrom(totalOrder)
			.where(totalOrder.user.eq(user)
				.and(totalOrder.deleted.eq(Deleted.UNDELETE)))
			.fetchOne();
		return Optional.ofNullable(result);
	}
}
