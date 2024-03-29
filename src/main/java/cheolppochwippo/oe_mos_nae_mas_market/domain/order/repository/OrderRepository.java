package cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
