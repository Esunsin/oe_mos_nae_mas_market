package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentCancelRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentJsonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponses;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentSuccessResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.service.PaymentServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
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

	@RequestMapping(value = "/payments/confirm")
	public ResponseEntity<CommonResponse<PaymentSuccessResponse>> confirmPayment(
		@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PaymentRequest request) {
		PaymentSuccessResponse response = paymentService.confirmPayment(userDetails.getUser(),
			request);
		return ResponseEntity.status(response.getCode()).body(CommonResponse.<PaymentSuccessResponse>builder()
			.msg("confirm payment complete!")
			.data(response)
			.build());
	}

	@RequestMapping(value = "/payments/cancel")
	public ResponseEntity<CommonResponse<JSONObject>> cancelPayment(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody PaymentCancelRequest request) {
		PaymentJsonResponse response = paymentService.paymentCancel(userDetails.getUser(), request);
		return ResponseEntity.status(response.getCode()).body(CommonResponse.<JSONObject>builder()
			.msg("cancel payment complete!")
			.data(response.getJsonObject())
			.build());
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
		@RequestParam(defaultValue = "1") int page,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Page<PaymentResponses> paymentResponses = paymentService.getPayments(userDetails.getUser(),
			page);
		return ResponseEntity.ok().body(CommonResponse.<Page<PaymentResponses>>builder()
			.msg("show paymentPage complete!")
			.data(paymentResponses)
			.build());
	}

	@RequestMapping("/payments/confirm/pass")
	public ResponseEntity<CommonResponse<Void>> confirmPassPayment(
		@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PaymentRequest request) {
		TotalOrder totalOrder = paymentService.checkPayment(userDetails.getUser(), request);
		paymentService.successPayment(totalOrder, request);
		return ResponseEntity.ok().body(CommonResponse.<Void>builder()
			.msg("confirm Pass Payment complete!")
			.build());
	}

	//테스트용 실결제 승인을 제외시킨 결제 내역 생성 api
	// 같은 주문번호의 결제 내역을 계속생성되는 이유는 제한시키는 로직이 실결제 승인에 있기 때문
	@RequestMapping("/payments/cancel/pass")
	public ResponseEntity<CommonResponse<Void>> cancelPassPayment(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@RequestBody PaymentCancelRequest request) {
		Payment payment = paymentService.checkCancelPayment(userDetails.getUser(), request);
		paymentService.successCancelPayment(payment, request);
		return ResponseEntity.ok().body(CommonResponse.<Void>builder()
			.msg("cancel Pass Payment complete!")
			.build());
	}

}
