package cheolppochwippo.oe_mos_nae_mas_market.domain.order.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingleOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    SingleOrderResponse createOrderInCart(User user, Long quantity, Long productId);

    SingleOrderResponse deleteOrderInCart(User user, Long orderId);

    List<SingleOrderResponse> showOrdersInCart(User user);

    SingleOrderResponse updateQuantity(Long quantity, Long orderId);


}
