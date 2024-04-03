package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.QTotalOrder.totalOrder;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.QTotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class TotalOrderRepositoryCustomImpl implements TotalOrderRepositoryCustom {


	private final JpaConfig jpaConfig;

	private final EntityManager entityManager;

	@Override
	public Optional<TotalOrder> findByUserTotal(Long userId) {
		TotalOrder query = jpaConfig.jpaQueryFactory()
			.select(QTotalOrder.totalOrder)
			.from(QTotalOrder.totalOrder)
			.where(
				userIdEq(userId)
			).fetchOne();

		return Optional.ofNullable(query);
	}

	@Override
	public Optional<Tuple> getTotalInfoByUserId(Long userId) {
		Tuple query = jpaConfig.jpaQueryFactory()
			.select(QOrder.order.price.sum(),QOrder.order.count())
			.from(QOrder.order)
			.where(userIdEq(userId))
			.fetchOne();
		return Optional.ofNullable(query);
	}

	@Override
	public Optional<Tuple> getTotalNameUserId(Long userId) {
		Tuple query = jpaConfig.jpaQueryFactory()
			.select(QOrder.order.product.productName,QOrder.order.quantity)
			.from(QOrder.order)
			.where(userIdEq(userId))
			.limit(1)
			.fetchOne();
		return Optional.ofNullable(query);
	}

	@Override
	public Optional<TotalOrder> findTotalOrderByUndeleted(User user) {
		TotalOrder result = jpaConfig.jpaQueryFactory()
			.selectFrom(totalOrder)
			.where(totalOrder.user.eq(user)
				.and(totalOrder.deleted.eq(Deleted.UNDELETE)))
			.fetchOne();
		return Optional.ofNullable(result);
	}

	@Override
	@Transactional
	public Optional<Long> pushOrder(TotalOrder totalOrder,Long userId) {
		Long count = jpaConfig.jpaQueryFactory()
			.update(QOrder.order)
			.set(QOrder.order.totalOrder,totalOrder)
			.where(
				userIdEq(userId),
				QOrder.order.deleted.eq(Deleted.UNDELETE)
			).execute();
		entityManager.flush();
		entityManager.clear();
		return Optional.ofNullable(count);
	}

	@Override
	public Optional<TotalOrder> findByUserUndeleted(User user) {
		TotalOrder query = jpaConfig.jpaQueryFactory()
			.selectFrom(totalOrder)
			.where(
				totalOrder.user.eq(user),
				totalOrder.deleted.eq(Deleted.UNDELETE)
			).fetchOne();
		return Optional.ofNullable(query);
	}

	private BooleanExpression userIdEq(Long userId) {
		return Objects.nonNull(userId) ? QOrder.order.user.id.eq(userId) : null;
	}
}
