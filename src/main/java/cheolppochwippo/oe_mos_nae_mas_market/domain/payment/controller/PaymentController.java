package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentJsonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponses;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.service.PaymentServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentServiceImpl paymentService;

	@RequestMapping(value = "/confirm")
	public ResponseEntity<JSONObject> confirmPayment(
		@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PaymentRequest request)
		throws Exception {
		PaymentJsonResponse response = paymentService.confirmPayment(userDetails.getUser(),
			request);
		return ResponseEntity.status(response.getCode()).body(response.getJsonObject());
	}

	@GetMapping("/payments/{paymentId}")
	public ResponseEntity<CommonResponse<PaymentResponse>> getPayment(@PathVariable Long paymentId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		PaymentResponse paymentResponses = paymentService.getPayment(userDetails.getUser(),
			paymentId);
		return ResponseEntity.ok().body(CommonResponse.<PaymentResponse>builder()
			.msg("show payment complete!")
			.data(paymentResponses)
			.build());
	}

	@GetMapping("/payments")
	public ResponseEntity<CommonResponse<Page<PaymentResponses>>> getPayment(
		@RequestParam("page")int page,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Page<PaymentResponses> paymentResponses = paymentService.getPayments(userDetails.getUser(),page);
		return ResponseEntity.ok().body(CommonResponse.<Page<PaymentResponses>>builder()
			.msg("show paymentPage complete!")
			.data(paymentResponses)
			.build());
	}


}
