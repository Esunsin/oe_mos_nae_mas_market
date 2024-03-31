package cheolppochwippo.oe_mos_nae_mas_market.domain.order.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.OrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponse createOrderInCart(User user, Long quantity, Long productId);

    OrderResponse deleteOrderInCart(User user,Long orderId);

    List<OrderResponse> showOrdersInCart(User user);

    OrderResponse updateQuantity(Long quantity, Long orderId);

    SingOrderResponse showOrderDirect(Long quantity, Long productId);
}
