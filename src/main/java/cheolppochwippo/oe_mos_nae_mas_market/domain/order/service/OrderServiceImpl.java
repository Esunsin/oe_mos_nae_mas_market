package cheolppochwippo.oe_mos_nae_mas_market.domain.order.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingleOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository.OrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public SingleOrderResponse createOrderInCart(User user, Long quantity, Long productId) {
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        if (quantity>findProduct.getQuantity()){
            throw new IllegalArgumentException("상품 수량이 부족합니다.");
        }
        Order order;
        Optional<Order> orderByProductIdAndUserBeforeBuy = orderRepository.findOrderByProductIdAndUserBeforeBuy(user, productId);
        if (orderByProductIdAndUserBeforeBuy.isEmpty()){
            order = new Order(quantity,findProduct,user);
            orderRepository.save(order);
        }
        else {
            order = orderByProductIdAndUserBeforeBuy.get();
            if(order.getQuantity() + quantity > findProduct.getQuantity()){
                throw new IllegalArgumentException("상품 수량이 부족합니다.");
            }
            order.updateQuantity(order.getQuantity()+quantity);
        }
        return new SingleOrderResponse(order);
    }

    @Transactional
    public SingleOrderResponse updateQuantity(Long quantity, Long orderId) {
        if(quantity < 1){
            throw new IllegalArgumentException("최소 수량은 1개 입니다.");
        }
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        Product findProduct = productRepository.findById(order.getProduct().getId()).orElseThrow();
        if (findProduct.getQuantity() < quantity){
            throw new IllegalArgumentException("상품 수량이 부족합니다.");
        }
        order.updateQuantity(quantity);

        return new SingleOrderResponse(order);
    }

    @Transactional
    public SingleOrderResponse deleteOrderInCart(User user, Long orderId){
        //단건 주문 검색
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("없는 주문입니다."));
        //자신의 주문인지 아닌지
        if (user.equals(findOrder.getUser())){
            throw new IllegalArgumentException("자신의 주문이 아닙니다.");
        }

        orderRepository.delete(findOrder);

        return  new SingleOrderResponse(findOrder);
    }

    @Transactional(readOnly = true)
    public List<SingleOrderResponse> showOrdersInCart(User user) {
        List<Order> orders = orderRepository.findOrderByUserBeforeBuy(user);
        return orders.stream().map(SingleOrderResponse::new).toList();
    }

    public SingOrderResponse showOrderDirect(Long quantity,Long productId){
        Product findProduct = productRepository.findById(productId).orElseThrow();
        return new SingOrderResponse(findProduct.getProductName(), quantity, findProduct.getPrice() * quantity);
    }
}
