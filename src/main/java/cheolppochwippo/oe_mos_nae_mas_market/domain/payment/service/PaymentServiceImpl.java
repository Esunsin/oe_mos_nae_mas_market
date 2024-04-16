package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.Delivery;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.repository.DeliveryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service.IssuedServiceImpl;
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
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InvalidUrlException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.ParsedException;
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
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.MessageSource;
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

	private final RedissonClient redissonClient;

	private final MessageSource messageSource;

	private final IssuedServiceImpl issuedService;

	@Override
	public PaymentJsonResponse confirmPayment(User user, PaymentRequest request) {
		TotalOrder totalOrder = checkPayment(user, request);
		JSONObject obj = new JSONObject();
		obj.put("orderId", request.getOrderId());
		obj.put("amount", request.getAmount());
		obj.put("paymentKey", request.getPaymentKey());

		String authorizations = getAuthorizations();
		HttpURLConnection connection = getConnection(
			"https://api.tosspayments.com/v1/payments/confirm", authorizations, "POST");

		int code = getCode(obj, connection);
		boolean isSuccess = code == 200;

		if (isSuccess) {
			successPayment(totalOrder, request);
		} else {
			failPayment(totalOrder, request);
		}

		JSONObject jsonObject = getJSONObject(connection, isSuccess);

		return new PaymentJsonResponse(jsonObject, code);
	}

	@Override
	public PaymentJsonResponse paymentCancel(User user, PaymentCancelRequest paymentCancelRequest) {
		Payment payment = checkCancelPayment(user, paymentCancelRequest);
		JSONObject obj = new JSONObject();
		obj.put("cancelReason", paymentCancelRequest.getCancelReason());

		String authorizations = getAuthorizations();

		HttpURLConnection connection = getConnection(
			"https://api.tosspayments.com/v1/payments/" + paymentCancelRequest.getPaymentKey()
				+ "/cancel", authorizations, "POST");

		int code = getCode(obj, connection);
		boolean isSuccess = code == 200;

		if (isSuccess) {
			successCancelPayment(payment, paymentCancelRequest);
		}

		JSONObject jsonObject = getJSONObject(connection, isSuccess);

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
			() -> new NoEntityException(
				messageSource.getMessage("noEntity.totalOrder", null, Locale.KOREA))
		);
		if (!Objects.equals(totalOrder.getPriceAmount(), paymentRequest.getAmount())
			|| !Objects.equals(totalOrder.getMerchantUid(), paymentRequest.getOrderId())) {
			throw new PriceMismatchException(
				messageSource.getMessage("price.mismatch", null, Locale.KOREA));
		}
		List<Order> orders = orderRepository.getOrdersFindTotalOrder(totalOrder);
		try {
			orders.parallelStream().forEach(productService::decreaseProductStock);
	} catch (InsufficientQuantityException e) {
		failPayment(totalOrder, paymentRequest);
		throw new InsufficientQuantityException(
			messageSource.getMessage("insufficient.quantity.product", null, Locale.KOREA));
	}
		return totalOrder;
}


	@Override
	public Payment checkCancelPayment(User user, PaymentCancelRequest paymentCancelRequest) {
		Payment payment = paymentRepository.findPaymentKey(paymentCancelRequest.getPaymentKey())
			.orElseThrow(
				() -> new NoEntityException(
					messageSource.getMessage("noEntity.payment", null, Locale.KOREA))
			);
		if (!Objects.equals(payment.getTotalOrder().getUser().getId(), user.getId())) {
			throw new NoPermissionException(
				messageSource.getMessage("noPermission.payment", null, Locale.KOREA));
		}
		return payment;
	}

	@Override
	public String getAuthorizations() {
		String widgetSecretKey = tossPaymentConfig.getSecretKey();
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode(
			(widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
		return "Basic " + new String(encodedBytes);
	}

	@Override
	public HttpURLConnection getConnection(String urls, String authorizations, String method) {
		try {
			URL url = new URL(urls);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", authorizations);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			return connection;
		} catch (IOException e) {
			throw new IllegalArgumentException("test");
		}
	}

	@Override
	public JSONObject getJSONObject(HttpURLConnection connection, boolean isSuccess) {
		try {
			InputStream responseStream =
				isSuccess ? connection.getInputStream() : connection.getErrorStream();

			Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
			JSONParser responseParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) responseParser.parse(reader);
			responseStream.close();
			return jsonObject;
		} catch (IOException e) {
			throw new InvalidUrlException(
				messageSource.getMessage("invalid.url", null, Locale.KOREA));
		} catch (ParseException e) {
			throw new ParsedException(messageSource.getMessage("parse", null, Locale.KOREA));
		}
	}

	@Override
	public int getCode(JSONObject obj, HttpURLConnection connection) {
		try {
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(obj.toString().getBytes("UTF-8"));

			return connection.getResponseCode();
		} catch (IOException e) {
			throw new InvalidUrlException(
				messageSource.getMessage("invalid.url", null, Locale.KOREA));
		}
	}

	@Override
	public PaymentResponse getPayment(User user, Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId).orElseThrow(
			() -> new NoEntityException(
				messageSource.getMessage("noEntity.payment", null, Locale.KOREA))
		);
		if (!Objects.equals(payment.getTotalOrder().getUser().getId(), user.getId())) {
			throw new NoPermissionException(
				messageSource.getMessage("noPermission.payment", null, Locale.KOREA));
		}
		return new PaymentResponse(payment);
	}

	@Override
	public Page<PaymentResponses> getPayments(User user, int page) {
		Pageable pageable = PageRequest.of(page, 10);
		return paymentRepository.getPaymentPageFindByUserId(user.getId(), pageable);
	}

}



