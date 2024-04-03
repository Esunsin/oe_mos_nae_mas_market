package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponses {
	private Long id;

	private Long amount;

	private String orderName;

	private String orderId;

	private PaymentStatementEnum statement;

	public PaymentResponses(Payment payment){
		id = payment.getPaymentId();
		amount = payment.getAmount();
		orderName = payment.getOrderName();
		statement = payment.getStatement();
	}
}
