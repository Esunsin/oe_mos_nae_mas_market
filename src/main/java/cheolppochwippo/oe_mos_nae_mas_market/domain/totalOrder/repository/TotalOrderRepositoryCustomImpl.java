package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.QTotalOrder.totalOrder;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.OrderStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderNameDto;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrdersGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.QTotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InsufficientQuantityException;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
	public Optional<TotalOrderNameDto> getTotalInfoByUserId(Long userId) {
		TotalOrderNameDto query = jpaConfig.jpaQueryFactory()
			.select(Projections.constructor(
				TotalOrderNameDto.class,
				QOrder.order.price.multiply(QOrder.order.quantity).sum(),
				QOrder.order.count(),
				QOrder.order.product.productName,
				QOrder.order.quantity
			))
			.from(QOrder.order)
			.where(userIdEq(userId),
				QOrder.order.deleted.eq(Deleted.UNDELETE),
				QOrder.order.statement.eq(OrderStatementEnum.ORDER)
			)
			.groupBy(QOrder.order.user.id)
			.fetchOne();
		return Optional.ofNullable(query);
	}

	@Override
	@Transactional
	public void completeOrder(TotalOrder totalOrder) {
		jpaConfig.jpaQueryFactory()
			.update(QOrder.order)
			.set(QOrder.order.deleted, Deleted.DELETE)
			.set(QOrder.order.statement, OrderStatementEnum.COMPLETE)
			.where(
				QOrder.order.totalOrder.id.eq(totalOrder.getId())
			)
			.execute();
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	@Transactional
	public void refundOrders(TotalOrder totalOrder) {
		jpaConfig.jpaQueryFactory()
			.update(QOrder.order)
			.set(QOrder.order.statement, OrderStatementEnum.REFUND)
			.where(
				QOrder.order.totalOrder.id.eq(totalOrder.getId())
			)
			.execute();
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public Page<TotalOrdersGetResponse> getTotalOrderPageFindByUserId(Long userId,
		Pageable pageable) {
		List<TotalOrder> query = jpaConfig.jpaQueryFactory()
			.selectFrom(totalOrder)
			.where(
				totalOrder.user.id.eq(userId),
				totalOrder.paymentStatementEnum.eq(PaymentStatementEnum.COMPLETE)
					.or(totalOrder.paymentStatementEnum.eq(PaymentStatementEnum.REFUND))
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(totalOrder.modifiedAt.desc())
			.fetch();
		List<TotalOrdersGetResponse> totalOrdersGetResponses = query.stream().map(
			TotalOrdersGetResponse::new).toList();
		return new PageImpl<>(totalOrdersGetResponses, pageable, getTotalOrderCount(userId));
	}

	private Long getTotalOrderCount(Long userId) {
		return jpaConfig.jpaQueryFactory()
			.select(totalOrder.count())
			.from(totalOrder)
			.where(
				totalOrder.user.id.eq(userId),
				totalOrder.paymentStatementEnum.eq(PaymentStatementEnum.COMPLETE)
					.or(totalOrder.paymentStatementEnum.eq(PaymentStatementEnum.REFUND))
			).fetchOne();
	}

	@Override
	public Optional<TotalOrder> findTotalOrderByUndeleted(User user) {
		TotalOrder result = jpaConfig.jpaQueryFactory()
			.selectFrom(totalOrder)
			.where(totalOrder.user.id.eq(user.getId())
				.and(totalOrder.deleted.eq(Deleted.UNDELETE)))
			.fetchOne();
		return Optional.ofNullable(result);
	}

	@Override
	@Transactional
	public Optional<Long> pushOrder(TotalOrder totalOrder, Long userId) {
		Long count = jpaConfig.jpaQueryFactory()
			.update(QOrder.order)
			.set(QOrder.order.totalOrder, totalOrder)
			.where(
				userIdEq(userId),
				QOrder.order.deleted.eq(Deleted.UNDELETE),
				QOrder.order.statement.eq(OrderStatementEnum.ORDER)
			).execute();
		entityManager.flush();
		entityManager.clear();
		return Optional.ofNullable(count);
	}

	@Override
	@Transactional
	public void decreaseQuantity(Long totalOrderId) {
		Long updatedQuantity = jpaConfig.jpaQueryFactory()
			.select(QOrder.order.id.count())
			.from(QOrder.order)
			.leftJoin(QOrder.order.product, QProduct.product)
			.where(
				QOrder.order.totalOrder.id.eq(totalOrderId),
				QOrder.order.product.quantity.subtract(QOrder.order.quantity).lt(0L)
			)
			.fetchOne();
		updatedQuantity = updatedQuantity != null ? updatedQuantity : 0L;
		if (updatedQuantity > 0) {
			throw new InsufficientQuantityException("");
		}
		jpaConfig.jpaQueryFactory()
			.update(QProduct.product)
			.set(QProduct.product.quantity, QProduct.product.quantity.subtract(
				JPAExpressions
					.select(QOrder.order.quantity)
					.from(QOrder.order)
					.where(QOrder.order.product.id.eq(QProduct.product.id)
						.and(QOrder.order.totalOrder.id.eq(totalOrderId)))
			))
			.where(QProduct.product.id.in(
					JPAExpressions
						.select(QOrder.order.product.id)
						.from(QOrder.order)
						.where(QOrder.order.totalOrder.id.eq(totalOrderId))
				)
			)
			.execute();

		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public Optional<TotalOrder> findByUserUndeleted(User user) {
		TotalOrder query = jpaConfig.jpaQueryFactory()
			.selectFrom(totalOrder)
			.where(
				totalOrder.user.id.eq(user.getId()),
				totalOrder.deleted.eq(Deleted.UNDELETE)
			).fetchOne();
		return Optional.ofNullable(query);
	}


	private BooleanExpression userIdEq(Long userId) {
		return Objects.nonNull(userId) ? QOrder.order.user.id.eq(userId) : null;
	}

}
