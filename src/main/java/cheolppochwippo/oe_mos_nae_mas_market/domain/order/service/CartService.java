package cheolppochwippo.oe_mos_nae_mas_market.domain.order.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInStoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInStoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingleOrderInCartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

	SingleOrderInCartResponse createOrderInCart(User user, Long quantity, Long productId);

	SingleOrderInCartResponse deleteOrderInCart(User user, Long orderId);

	List<SingleOrderInCartResponse> showOrdersInCart(User user);

	SingleOrderInCartResponse updateQuantity(Long quantity, Long orderId);

	SingleOrderInCartResponse createOrderByDirect(User user, Long quantity, Long productId);

	Long createOrderByCart(User user);

	List<AllOrderInStoreResponse> showOrdersInStore(User user, AllOrderInStoreRequest request);

	List<SingleOrderInCartResponse> getStateOrder(User user);

}
