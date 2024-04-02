package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.service.PaymentServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.TossPaymentConfig;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentServiceImpl paymentService;

	private final TossPaymentConfig tossPaymentConfig;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/confirm")
	public ResponseEntity<JSONObject> confirmPayment(@RequestBody PaymentRequest request)
		throws Exception {

		JSONObject obj = new JSONObject();
		obj.put("orderId", request.getOrderId());
		obj.put("amount", request.getAmount());
		obj.put("paymentKey", request.getPaymentKey());

		// 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
		// 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
		String widgetSecretKey = tossPaymentConfig.getSecretKey();
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode(
			(widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
		String authorizations = "Basic " + new String(encodedBytes);

		// 결제를 승인하면 결제수단에서 금액이 차감됩니다.
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
			System.out.println("test1");
		} else {
			System.out.println("test2");
		}
		// 결제 성공 및 실패 비즈니스 로직을 구현합니다.
		Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
		JSONParser responseParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) responseParser.parse(reader);
		responseStream.close();

		return ResponseEntity.status(code).body(jsonObject);
	}


}
