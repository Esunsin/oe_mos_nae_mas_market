package cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> ,OrderRepositoryCustom{

    List<Order> findByTotalOrder(TotalOrder totalOrder);

}
