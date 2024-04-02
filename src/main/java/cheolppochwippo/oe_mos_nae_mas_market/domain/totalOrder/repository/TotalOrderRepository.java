package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalOrderRepository  extends JpaRepository<TotalOrder, Long>,TotalOrderRepositoryCustom {
}
