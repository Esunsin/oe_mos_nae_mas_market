package cheolppochwippo.oe_mos_nae_mas_market.domain.order.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInCartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInStoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.AllOrderInStoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.CartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto.SingleOrderInCartResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.service.CartService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderInCartController {

	private final CartService cartService;

	@GetMapping("/health-check")
	public ResponseEntity<HttpStatus> healthCheck() {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/orders")
	public ResponseEntity<CartResponse<List<SingleOrderInCartResponse>, AllOrderInCartResponse>> showOrdersInCart(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<SingleOrderInCartResponse> ordersInCart = cartService.showOrdersInCart(
			userDetails.getUser());
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
		, @RequestParam Long quantity
		, @PathVariable("productId") Long productId) {
		SingleOrderInCartResponse orderInCart = cartService.createOrderInCart(userDetails.getUser(),
			quantity, productId);
		return ResponseEntity.status(HttpStatus.OK.value())
			.body(CommonResponse.<SingleOrderInCartResponse>builder()
				.msg("create order in cart")
				.data(orderInCart)
				.build());
	}

	@PutMapping("/orders/{orderId}")
	public ResponseEntity<CommonResponse<SingleOrderInCartResponse>> updateOrderQuantityInCart(
		@RequestParam Long quantity
		, @PathVariable("orderId") Long orderId) {
		SingleOrderInCartResponse updateQuantityOrder = cartService.updateQuantity(quantity,
			orderId);
		return ResponseEntity.status(HttpStatus.OK.value())
			.body(CommonResponse.<SingleOrderInCartResponse>builder()
				.msg("update order quantity in cart")
				.data(updateQuantityOrder)
				.build());
	}

	@DeleteMapping("/orders/{orderId}")
	public ResponseEntity<CommonResponse<SingleOrderInCartResponse>> deleteOrderInCart(
		@AuthenticationPrincipal UserDetailsImpl userDetails
		, @PathVariable("orderId") Long orderId) {
		SingleOrderInCartResponse deletedOrderInCart = cartService.deleteOrderInCart(
			userDetails.getUser(), orderId);
		return ResponseEntity.status(HttpStatus.OK.value())
			.body(CommonResponse.<SingleOrderInCartResponse>builder()
				.msg("delete order in cart")
				.data(deletedOrderInCart)
				.build());
	}

	@PostMapping("/carts/to-order")
	public ResponseEntity<CommonResponse<Long>> createOrderByCart(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long count = cartService.createOrderByCart(userDetails.getUser());
		return ResponseEntity.status(HttpStatus.OK.value())
			.body(CommonResponse.<Long>builder()
				.msg("create order in cart")
				.data(count)
				.build());
	}

	@PostMapping("/products/{productId}/to-order")
	public ResponseEntity<CommonResponse<SingleOrderInCartResponse>> createOrderByDirect(
		@AuthenticationPrincipal UserDetailsImpl userDetails
		, @RequestParam Long quantity
		, @PathVariable("productId") Long productId) {
		SingleOrderInCartResponse response = cartService.createOrderByDirect(userDetails.getUser(),
			quantity, productId);
		return ResponseEntity.status(HttpStatus.OK.value())
			.body(CommonResponse.<SingleOrderInCartResponse>builder()
				.msg("create order in product")
				.data(response)
				.build());
	}

	@GetMapping("/store/orders")
	public ResponseEntity<CommonResponse<List<AllOrderInStoreResponse>>> showOrderInStore(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody AllOrderInStoreRequest request) {
		List<AllOrderInStoreResponse> responses = cartService.showOrdersInStore(
			userDetails.getUser(), request);
		return ResponseEntity.status(HttpStatus.OK.value())
			.body(CommonResponse.<List<AllOrderInStoreResponse>>builder()
				.msg("view order in store")
				.data(responses)
				.build());
	}

}
