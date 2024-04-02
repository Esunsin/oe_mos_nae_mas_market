package cheolppochwippo.oe_mos_nae_mas_market.domain.order.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.CartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingleOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.service.OrderService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<CartResponse<List<SingleOrderResponse>,AllOrderResponse>> showOrdersInCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<SingleOrderResponse> ordersInCart = orderService.showOrdersInCart(userDetails.getUser());
        AllOrderResponse allOrderResponse = new AllOrderResponse(ordersInCart);
        return ResponseEntity.status(HttpStatus.OK.value())
        .body(CartResponse.<List<SingleOrderResponse>,AllOrderResponse>builder()
                .msg("show order in cart")
                .data(ordersInCart)
                .view(allOrderResponse)
                .build());
    }

    @PostMapping("/products/{productId}/orders")
    public ResponseEntity<CommonResponse<SingleOrderResponse>> createOrderInCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ,@RequestParam Long quantity
            ,@PathVariable("productId") Long productId){
        SingleOrderResponse orderInCart = orderService.createOrderInCart(userDetails.getUser(), quantity, productId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<SingleOrderResponse>builder()
                        .msg("create order in cart")
                        .data(orderInCart)
                        .build());
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<CommonResponse<SingleOrderResponse>> updateOrderQuantityInCart(
            @RequestParam Long quantity
            ,@PathVariable("orderId") Long orderId){
        SingleOrderResponse updateQuantityOrder = orderService.updateQuantity(quantity, orderId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<SingleOrderResponse>builder()
                        .msg("update order quantity in cart")
                        .data(updateQuantityOrder)
                        .build());
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<CommonResponse<SingleOrderResponse>> deleteOrderInCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ,@PathVariable("orderId") Long orderId){
        SingleOrderResponse deletedOrderInCart = orderService.deleteOrderInCart(userDetails.getUser(), orderId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<SingleOrderResponse>builder()
                        .msg("delete order in cart")
                        .data(deletedOrderInCart)
                        .build());
    }

}