package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.QTotalOrder.totalOrder;

@RequiredArgsConstructor
public class TotalOrderRepositoryCustomImpl implements TotalOrderRepositoryCustom{

    private final JpaConfig jpaConfig;

    public Optional<TotalOrder> findTotalOrderByUndeleted(User user){
        TotalOrder result = jpaConfig.jpaQueryFactory()
                .selectFrom(totalOrder)
                .where(totalOrder.user.eq(user)
                        .and(totalOrder.deleted.eq(Deleted.UNDELETE)))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
