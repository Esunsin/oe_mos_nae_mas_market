package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {

	private Long id;

	private Long amount;

	private String orderName;

	private String orderId;

	private boolean paySuccessYN;

	private String paymentKey;

	private String failReason;

	private boolean cancelYN;

	private String cancelReason;

	private PaymentStatementEnum statement;

	public PaymentResponse(Payment payment){
		id = payment.getPaymentId();
		amount = payment.getAmount();
		orderName = payment.getOrderName();
		paySuccessYN = payment.isPaySuccessYN();
		paymentKey = payment.getPaymentKey();
		cancelYN = payment.isCancelYN();
		cancelReason = payment.getCancelReason();
		statement = payment.getStatement();
	}
}
