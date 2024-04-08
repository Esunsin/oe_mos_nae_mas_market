package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.Delivery;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.repository.DeliveryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository.OrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentCancelRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentJsonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponses;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.repository.PaymentRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.TossPaymentConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InsufficientQuantityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.PriceMismatchException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	private final DeliveryRepository deliveryRepository;

	private final TotalOrderRepository totalOrderRepository;

	private final IssuedRepository issuedRepository;

	private final OrderRepository orderRepository;

	private final TossPaymentConfig tossPaymentConfig;

	private final ProductServiceImpl productService;

	@Override
	public PaymentJsonResponse confirmPayment(User user, PaymentRequest request)
		throws IOException, ParseException {
		TotalOrder totalOrder = checkPayment(user, request);
		JSONObject obj = new JSONObject();
		obj.put("orderId", request.getOrderId());
		obj.put("amount", request.getAmount());
		obj.put("paymentKey", request.getPaymentKey());

		String widgetSecretKey = tossPaymentConfig.getSecretKey();
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode(
			(widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
		String authorizations = "Basic " + new String(encodedBytes);

		URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Authorization", authorizations);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(obj.toString().getBytes("UTF-8"));

		int code = connection.getResponseCode();
		boolean isSuccess = code == 200;

		InputStream responseStream =
			isSuccess ? connection.getInputStream() : connection.getErrorStream();
		if (isSuccess) {
			successPayment(totalOrder, request);
		} else {
			failPayment(totalOrder, request);
		}
		Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
		JSONParser responseParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) responseParser.parse(reader);
		responseStream.close();
		return new PaymentJsonResponse(jsonObject, code);
	}

	@Override
	public PaymentJsonResponse paymentCancel(User user, PaymentCancelRequest paymentCancelRequest)
		throws IOException, ParseException {
		Payment payment = checkCancelPayment(user, paymentCancelRequest);
		JSONObject obj = new JSONObject();
		obj.put("cancelReason", paymentCancelRequest.getCancelReason());

		String widgetSecretKey = tossPaymentConfig.getSecretKey();
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode(
			(widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
		String authorizations = "Basic " + new String(encodedBytes);

		URL url = new URL(
			"https://api.tosspayments.com/v1/payments/" + paymentCancelRequest.getPaymentKey()
				+ "/cancel");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Authorization", authorizations);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(obj.toString().getBytes("UTF-8"));

		int code = connection.getResponseCode();
		boolean isSuccess = code == 200;

		if (isSuccess) {
			successCancelPayment(payment, paymentCancelRequest);
		}

		InputStream responseStream =
			isSuccess ? connection.getInputStream() : connection.getErrorStream();

		Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
		JSONParser responseParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) responseParser.parse(reader);
		responseStream.close();
		return new PaymentJsonResponse(jsonObject, code);
	}

	@Override
	@Transactional
	public void successPayment(TotalOrder totalOrder, PaymentRequest paymentRequest) {
		totalOrder.completeOrder();
		totalOrderRepository.save(totalOrder);
		totalOrderRepository.completeOrder(totalOrder);
		if (totalOrder.getIssueId() != 0) {
			issuedRepository.setDeletedFindById(totalOrder.getIssueId());
		}
		Payment payment = new Payment(paymentRequest, totalOrder);
		paymentRepository.save(payment);
		Delivery delivery = new Delivery(totalOrder);
		deliveryRepository.save(delivery);
	}


	@Override
	public void successCancelPayment(Payment payment, PaymentCancelRequest paymentCancelRequest) {
		Payment cancelPayment = new Payment(payment, paymentCancelRequest);
		paymentRepository.save(cancelPayment);
	}

	@Override
	public void failPayment(TotalOrder totalOrder, PaymentRequest paymentRequest) {
		totalOrder.cancelInProgressOrder();
		totalOrderRepository.save(totalOrder);
	}

	@Override
	public TotalOrder checkPayment(User user, PaymentRequest paymentRequest) {
		TotalOrder totalOrder = totalOrderRepository.findTotalOrderByUndeleted(user).orElseThrow(
			() -> new NoEntityException("진행중인 주문이 없습니다.")
		);
		if (!Objects.equals(totalOrder.getPriceAmount(), paymentRequest.getAmount())
			|| !Objects.equals(totalOrder.getMerchantUid(), paymentRequest.getOrderId())) {
			throw new PriceMismatchException("올바르지 않은 요청 입니다.");
		}
		List<Order> orders = orderRepository.getOrdersFindTotalOrder(totalOrder);
		try {
			orders.parallelStream().forEach(productService::decreaseProductStock);
			//stream
		} catch (IllegalArgumentException e) {
			failPayment(totalOrder, paymentRequest);
			throw new InsufficientQuantityException("재고가 부족합니다.");
		}
		return totalOrder;
	}

	@Override
	public Payment checkCancelPayment(User user, PaymentCancelRequest paymentCancelRequest) {
		Payment payment = paymentRepository.findPaymentKey(paymentCancelRequest.getPaymentKey())
			.orElseThrow(
				() -> new NoEntityException("존재하지 않는 결제번호 입니다.")
			);
		if (!Objects.equals(payment.getTotalOrder().getUser().getId(), user.getId())) {
			throw new NoPermissionException("해당 결제를 취소하실 권한이 없습니다.");
		}
		return payment;
	}

	@Override
	public PaymentResponse getPayment(User user, Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId).orElseThrow(
			() -> new NoEntityException("존재하지 않는 결제정보 입니다.")
		);
		if (!Objects.equals(payment.getTotalOrder().getUser().getId(), user.getId())) {
			throw new NoPermissionException("조회하실 권한이 없습니다.");
		}
		return new PaymentResponse(payment);
	}

	@Override
	public Page<PaymentResponses> getPayments(User user, int page) {
		Pageable pageable = PageRequest.of(page, 10);
		return paymentRepository.getPaymentPageFindByUserId(user.getId(), pageable);
	}

}



