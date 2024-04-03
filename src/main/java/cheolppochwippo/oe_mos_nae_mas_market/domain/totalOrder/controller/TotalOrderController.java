package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrdersGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service.TotalOrderServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TotalOrderController {

	private final TotalOrderServiceImpl totalOrderService;

	//주문 요청시 totalOrder 생성
	@PostMapping("/totalOrders")
	public ResponseEntity<CommonResponse<TotalOrderResponse>> createTotalOrder(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody TotalOrderRequest request,
		Model model) {
		TotalOrderResponse totalOrderResponse = totalOrderService.createTotalOrder(
			userDetails.getUser(), request);
		model.addAttribute("t", totalOrderResponse);
		return ResponseEntity.ok()
			.body(CommonResponse.<TotalOrderResponse>builder()
				.data(totalOrderResponse)
				.msg("create totalOrder")
				.build());
	}

	@GetMapping("/totalOrders/{totalOrderId}")
	public ResponseEntity<CommonResponse<TotalOrderGetResponse>> getTotalOrder(
		@PathVariable Long totalOrderId, @AuthenticationPrincipal UserDetailsImpl userDetails){
		TotalOrderGetResponse totalOrderResponse = totalOrderService.getTotalOrder(userDetails.getUser(),totalOrderId);
		return ResponseEntity.ok().body(CommonResponse.<TotalOrderGetResponse>builder()
			.msg("show totalOrder complete!")
			.data(totalOrderResponse)
			.build());
	}

	@GetMapping("/totalOrders")
	public ResponseEntity<CommonResponse<Page<TotalOrdersGetResponse>>> getTotalOrders(
		@RequestParam(name = "page")int page,@AuthenticationPrincipal UserDetailsImpl userDetails){
		Page<TotalOrdersGetResponse> totalOrdersGetResponses = totalOrderService.getTotalOrders(userDetails.getUser(),page);
		return ResponseEntity.ok().body(CommonResponse.<Page<TotalOrdersGetResponse>>builder()
			.msg("show totalOrders complete!")
			.data(totalOrdersGetResponses)
			.build());
	}

}
