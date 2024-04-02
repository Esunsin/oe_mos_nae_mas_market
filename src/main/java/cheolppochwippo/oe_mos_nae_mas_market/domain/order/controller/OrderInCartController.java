package cheolppochwippo.oe_mos_nae_mas_market.domain.order.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInCartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.CartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingleOrderInCartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.service.CartService;
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
public class OrderInCartController {

    private final CartService orderService;

    @GetMapping("/orders")
    public ResponseEntity<CartResponse<List<SingleOrderInCartResponse>, AllOrderInCartResponse>> showOrdersInCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<SingleOrderInCartResponse> ordersInCart = orderService.showOrdersInCart(userDetails.getUser());
        AllOrderInCartResponse allOrderResponse = new AllOrderInCartResponse(ordersInCart);
        return ResponseEntity.status(HttpStatus.OK.value())
        .body(CartResponse.<List<SingleOrderInCartResponse>, AllOrderInCartResponse>builder()
                .msg("show order in cart")
                .data(ordersInCart)
                .view(allOrderResponse)
                .build());
    }

    @PostMapping("/products/{productId}/orders")
    public ResponseEntity<CommonResponse<SingleOrderInCartResponse>> createOrderInCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ,@RequestParam Long quantity
            ,@PathVariable("productId") Long productId){
        SingleOrderInCartResponse orderInCart = orderService.createOrderInCart(userDetails.getUser(), quantity, productId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<SingleOrderInCartResponse>builder()
                        .msg("create order in cart")
                        .data(orderInCart)
                        .build());
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<CommonResponse<SingleOrderInCartResponse>> updateOrderQuantityInCart(
            @RequestParam Long quantity
            ,@PathVariable("orderId") Long orderId){
        SingleOrderInCartResponse updateQuantityOrder = orderService.updateQuantity(quantity, orderId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<SingleOrderInCartResponse>builder()
                        .msg("update order quantity in cart")
                        .data(updateQuantityOrder)
                        .build());
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<CommonResponse<SingleOrderInCartResponse>> deleteOrderInCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ,@PathVariable("orderId") Long orderId){
        SingleOrderInCartResponse deletedOrderInCart = orderService.deleteOrderInCart(userDetails.getUser(), orderId);
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.<SingleOrderInCartResponse>builder()
                        .msg("delete order in cart")
                        .data(deletedOrderInCart)
                        .build());
    }

}