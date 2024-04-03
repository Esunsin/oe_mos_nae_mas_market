package cheolppochwippo.oe_mos_nae_mas_market.domain.order.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingleOrderInCartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    SingleOrderInCartResponse createOrderInCart(User user, Long quantity, Long productId);

    SingleOrderInCartResponse deleteOrderInCart(User user, Long orderId);

    List<SingleOrderInCartResponse> showOrdersInCart(User user);

    SingleOrderInCartResponse updateQuantity(Long quantity, Long orderId);


}
