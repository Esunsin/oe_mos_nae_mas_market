package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentCancelRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentJsonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponses;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;

public interface PaymentService {

	PaymentJsonResponse confirmPayment(User user, PaymentRequest request)
		throws IOException, ParseException;

	void successPayment(TotalOrder totalOrder, PaymentRequest paymentRequest);

	void failPayment(TotalOrder totalOrder,PaymentRequest paymentRequest);

	TotalOrder checkPayment(User user,PaymentRequest paymentRequest);

	PaymentResponse getPayment(User user,Long paymentId);

	void successCancelPayment(Payment payment,PaymentCancelRequest paymentCancelRequest);

	Page<PaymentResponses> getPayments(User user,int page);

	Payment checkCancelPayment(User user,PaymentCancelRequest paymentCancelRequest);

	PaymentJsonResponse paymentCancel(User user,PaymentCancelRequest paymentCancelRequest)
		throws IOException, ParseException;

}
