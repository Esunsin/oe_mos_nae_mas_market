package cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder.order;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct.product;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private final JpaConfig jpaConfig;

    public List<Order> findOrderByUserBeforeBuy(User user){
        return jpaConfig.jpaQueryFactory()
                .selectFrom(order)
                .leftJoin(order.product,product).fetchJoin()
                .where(order.deleted.eq(Deleted.UNDELETE)
                        .and(order.user.eq(user)))
                .fetch();
    }

    public Order findOrderByUserBeforeBuyDirect(User user){
        return jpaConfig.jpaQueryFactory()
                .selectFrom(order)
                .leftJoin(product).fetchJoin()
                .where(order.deleted.eq(Deleted.DIRECT)
                        .and(order.user.eq(user)))
                .fetchOne();
    }

    public Optional<Order> findOrderByProductIdAndUserBeforeBuy(User user, Long productId){
        return Optional.ofNullable(
                jpaConfig.jpaQueryFactory()
                .selectFrom(order)
                .where(order.deleted.eq(Deleted.UNDELETE)
                        .and(order.user.eq(user))
                        .and(order.product.id.eq(productId)))
                .fetchOne());
    }
    public Optional<Order> findOrderByProductIdAndUserBeforeBuyDirect(User user, Long productId){
        return Optional.ofNullable(
                jpaConfig.jpaQueryFactory()
                        .selectFrom(order)
                        .where(order.deleted.eq(Deleted.DIRECT)
                                .and(order.user.eq(user))
                                .and(order.product.id.eq(productId)))
                        .fetchOne());
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
}
