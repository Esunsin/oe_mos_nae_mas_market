package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;

import java.util.Optional;

public interface TotalOrderRepositoryCustom {

	Optional<TotalOrder> findByUserTotal(Long userId);

	Long getTotalPriceByUserId(Long userId);
    Optional<TotalOrder> findTotalOrderByUndeleted(User user);

}
