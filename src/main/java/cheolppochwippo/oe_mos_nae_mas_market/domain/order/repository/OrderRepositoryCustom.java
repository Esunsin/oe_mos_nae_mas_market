package cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInStoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInStoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {

	List<Order> findOrderByUserBeforeBuy(User user);

	Optional<Order> findOrderByProductIdAndUserBeforeBuy(User user, Long productId);

	Long updateCartToOrder(Long userId);

	List<Order> getOrdersFindTotalOrder(TotalOrder totalOrder);

	void deleteOrders(Long userId);

	List<AllOrderInStoreResponse> findOrderByUserStore(Long userId, AllOrderInStoreRequest request);

	List<Order> getStateOrders(Long userId);

}
