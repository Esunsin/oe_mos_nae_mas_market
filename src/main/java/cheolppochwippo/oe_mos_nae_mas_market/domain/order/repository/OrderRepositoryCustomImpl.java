package cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.QOrder.order;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private final JpaConfig jpaConfig;

    public List<Order> findOrderByUserBeforeBuy(User user){
        return jpaConfig.jpaQueryFactory()
                .selectFrom(order)
                .where(order.deleted.eq(Deleted.UNDELETE)
                        .and(order.user.eq(user)))
                .fetch();
    }
}
