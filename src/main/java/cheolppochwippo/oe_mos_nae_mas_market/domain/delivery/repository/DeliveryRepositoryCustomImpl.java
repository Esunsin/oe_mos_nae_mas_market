package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.QDelivery.delivery;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.Delivery;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeliveryRepositoryCustomImpl implements DeliveryRepositoryCustom {

    private final JpaConfig jpaConfig;

    @Override
    public List<Delivery> findDeliveryByUser(User user) {
        return jpaConfig.jpaQueryFactory()
            .selectFrom(delivery)
            .leftJoin(delivery.user).fetchJoin()
            .where(delivery.deleted.eq(Deleted.UNDELETE)
                .and(delivery.user.eq(user)))
            .fetch();
    }
}
