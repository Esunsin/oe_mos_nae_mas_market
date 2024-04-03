package cheolppochwippo.oe_mos_nae_mas_market.domain.order;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingleOrderInCartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository.OrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.service.CartService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.service.CartServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductRepository productRepository;

    CartService cartService;

    @BeforeEach
    void before(){
        cartService = new CartServiceImpl(orderRepository, productRepository);
    }

    @Test
    @DisplayName("오더 생성 - 카트")
    void createOrder(){
        //given
        Product product = new Product(100L, "productA","best Product", 12000L, 10000L, 2000L, 10L, Deleted.UNDELETE, null);
        User customer = new User(100L, "customer", "CONSUMER");
        Long quantity = 9L;

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        //when
        SingleOrderInCartResponse orderInCart = cartService.createOrderInCart(customer, quantity, product.getId());
        //then
        Assertions.assertEquals(orderInCart.getPrice(),product.getPrice());
        Assertions.assertEquals(orderInCart.getQuantity(),quantity);
    }
    @Test
    @DisplayName("오더 생성 - 같은 상품을 다시 오더 할때")
    void createSameOrder(){
        //given
        Product product = new Product(100L, "productA","best Product", 12000L, 10000L, 2000L, 10L, Deleted.UNDELETE, null);
        User customer = new User(100L, "customer", "CONSUMER");
        Long quantity = 3L;
        Order order = new Order(quantity, product, customer);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(orderRepository.findOrderByProductIdAndUserBeforeBuy(customer, product.getId())).willReturn(Optional.of(order));

        //when
        SingleOrderInCartResponse orderInCart = cartService.createOrderInCart(customer, quantity, product.getId());

        //then
        Assertions.assertEquals(orderInCart.getPrice(),product.getPrice());
        Assertions.assertEquals(orderInCart.getQuantity(),quantity*2);
    }

    @Test
    @DisplayName("오더 생성 - 재고보다 많을때")
    void overQuantity(){
        //given
        Product product = new Product(100L, "productA","best Product", 12000L, 10000L, 2000L, 10L, Deleted.UNDELETE, null);
        User customer = new User(100L, "customer", "CONSUMER");
        Long quantity = 11L;

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        CartService cartService = new CartServiceImpl(orderRepository, productRepository);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> cartService.createOrderInCart(customer, quantity, product.getId()));
    }

    @Test
    @DisplayName("오더 수량 변경")
    void updateQuantity(){
        //given
        Product product = new Product(100L, "productA","best Product", 12000L, 10000L, 2000L, 10L, Deleted.UNDELETE, null);
        User customer = new User(100L, "customer", "CONSUMER");
        Long quantity = 9L;
        Order order = new Order(quantity, product, customer);
        Long updateQuantity = 5L;
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        //when
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));

        SingleOrderInCartResponse singleOrderInCartResponse = cartService.updateQuantity(updateQuantity, 100L);

        //then
        Assertions.assertEquals(updateQuantity,singleOrderInCartResponse.getQuantity());
    }

    @Test
    @DisplayName("오더 수량 변경 - 초과")
    void updateOverQuantity(){
        //given
        Product product = new Product(100L, "productA","best Product", 12000L, 10000L, 2000L, 10L, Deleted.UNDELETE, null);
        User customer = new User(100L, "customer", "CONSUMER");
        Long quantity = 9L;
        Order order = new Order(quantity, product, customer);
        Long updateQuantity = 12L;
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        //when
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));

        //then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> cartService.updateQuantity(updateQuantity, 100L));
    }
    @Test
    @DisplayName("오더 수량 변경 - 1보다 작은 값")
    void updateLessQuantity(){
        //given
        Long updateQuantity = 0L;

        //then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> cartService.updateQuantity(updateQuantity, 100L));
    }

}
