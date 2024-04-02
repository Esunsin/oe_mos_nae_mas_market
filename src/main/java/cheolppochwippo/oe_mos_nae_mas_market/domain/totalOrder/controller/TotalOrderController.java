package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service.TotalOrderServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TotalOrderController {

	private final TotalOrderServiceImpl totalOrderService;

	@PostMapping("/totalOrder")
	public ResponseEntity<CommonResponse<TotalOrderResponse>> createTotalOrder(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TotalOrderRequest request) {
		TotalOrderResponse totalOrderResponse = totalOrderService.createTotalOrder(userDetails.getUser(),request);
		return ResponseEntity.ok()
			.body(CommonResponse.<TotalOrderResponse>builder()
				.data(totalOrderResponse)
				.msg("테스트성공")
				.build());
	}
}
