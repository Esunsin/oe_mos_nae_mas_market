package cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder.order;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.OrderStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

	private final JpaConfig jpaConfig;

	private final EntityManager entityManager;

	public List<Order> findOrderByUserBeforeBuy(User user) {
		return jpaConfig.jpaQueryFactory()
			.selectFrom(order)
			.where(order.deleted.eq(Deleted.UNDELETE)
				.and(order.user.eq(user))
				.and(order.cartYN.eq(true)))
			.fetch();
	}

	public Optional<Order> findOrderByProductIdAndUserBeforeBuy(User user, Long productId) {
		return Optional.ofNullable(
			jpaConfig.jpaQueryFactory()
				.selectFrom(order)
				.where(order.deleted.eq(Deleted.UNDELETE)
					.and(order.user.eq(user))
					.and(order.product.id.eq(productId))
					.and(order.cartYN.eq(true)))
				.fetchOne());
	}

	@Override
	@Transactional
	public Long updateCartToOrder(Long userId) {
		Long count = jpaConfig.jpaQueryFactory()
			.update(QOrder.order)
			.set(QOrder.order.statement, OrderStatementEnum.ORDER)
			.where(
				order.user.id.eq(userId),
				order.cartYN.eq(true),
				order.deleted.eq(Deleted.UNDELETE)
			)
			.execute();
		entityManager.flush();
		entityManager.clear();
		return count;
	}

	@Override
	public List<Order> getOrdersFindTotalOrder(TotalOrder totalOrder) {
		List<Order> query = jpaConfig.jpaQueryFactory()
			.select(QOrder.order)
			.from(QOrder.order)
			.where(
				QOrder.order.totalOrder.id.eq(totalOrder.getId())
			).fetch();
		return query;
	}

	@Override
	@Transactional
	public void deleteOrders(Long userId) {
		jpaConfig.jpaQueryFactory()
			.update(QOrder.order)
			.set(order.deleted, Deleted.DELETE)
			.set(order.statement,OrderStatementEnum.CANCEL)
			.where(
				order.user.id.eq(userId),
				order.deleted.eq(Deleted.UNDELETE),
				order.cartYN.eq(false)
			)
			.execute();

		jpaConfig.jpaQueryFactory()
			.update(QOrder.order)
			.set(order.statement,OrderStatementEnum.WAIT)
			.where(
				order.user.id.eq(userId),
				order.deleted.eq(Deleted.UNDELETE),
				order.statement.eq(OrderStatementEnum.ORDER),
				order.cartYN.eq(true)
			)
			.execute();
		entityManager.flush();
		entityManager.clear();
	}
}
