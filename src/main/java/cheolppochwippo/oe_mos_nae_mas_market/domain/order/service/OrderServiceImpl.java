package cheolppochwippo.oe_mos_nae_mas_market.domain.order.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.OrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository.OrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrderInCart(User user, Long quantity, Long productId) {
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Order order = new Order(quantity,findProduct,user);
        orderRepository.save(order);

        return new OrderResponse(order);
    }

    @Transactional
    public OrderResponse updateQuantity(Long quantity, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        order.updateQuantity(quantity);

        return new OrderResponse(order);
    }

    @Transactional
    public OrderResponse deleteOrderInCart(User user,Long orderId){
        //단건 주문 검색
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("없는 주문입니다."));
        //자신의 주문인지 아닌지
        if (user.equals(findOrder.getUser())){
            throw new IllegalArgumentException("자신의 주문이 아닙니다.");
        }

        orderRepository.delete(findOrder);

        return  new OrderResponse(findOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> showOrdersInCart(User user) {
        List<Order> orders = orderRepository.findOrderByUserBeforeBuy(user);
        return orders.stream().map(OrderResponse::new).toList();
    }
}
