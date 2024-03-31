package cheolppochwippo.oe_mos_nae_mas_market.domain.order.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.OrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.service.OrderService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<CommonResponse<List<OrderResponse>>> showOrdersInCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<OrderResponse> ordersInCart = orderService.showOrdersInCart(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
        .body(CommonResponse.<List<OrderResponse>>builder()
                .msg("show order in cart")
                .data(ordersInCart)
                .build());
    }

    @PostMapping("/products/{productId}/orders")
    public ResponseEntity<CommonResponse<OrderResponse>> createOrderInCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ,@RequestParam Long quantity
            ,@PathVariable("productId") Long productId){
        OrderResponse orderInCart = orderService.createOrderInCart(userDetails.getUser(), quantity, productId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<OrderResponse>builder()
                        .msg("create order in cart")
                        .data(orderInCart)
                        .build());
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> updateOrderQuantityInCart(
            @RequestParam Long quantity
            ,@PathVariable("orderId") Long orderId){
        OrderResponse updateQuantityOrder = orderService.updateQuantity(quantity, orderId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<OrderResponse>builder()
                        .msg("update order quantity in cart")
                        .data(updateQuantityOrder)
                        .build());
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> deleteOrderInCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ,@PathVariable("orderId") Long orderId){
        OrderResponse deletedOrderInCart = orderService.deleteOrderInCart(userDetails.getUser(), orderId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<OrderResponse>builder()
                        .msg("delete order in cart")
                        .data(deletedOrderInCart)
                        .build());
    }
    //바로 구매
    @GetMapping("/products/{productId}/orders")
    public ResponseEntity<CommonResponse<SingOrderResponse>> orderDirect(
            @RequestParam Long quantity
            ,@PathVariable("productId") Long productId
    ){
        SingOrderResponse singOrderResponse = orderService.showOrderDirect(quantity, productId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<SingOrderResponse>builder()
                        .msg("show product direct")
                        .data(singOrderResponse)
                        .build());
    }
}