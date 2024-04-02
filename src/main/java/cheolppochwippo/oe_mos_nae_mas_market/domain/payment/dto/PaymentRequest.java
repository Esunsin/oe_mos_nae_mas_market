package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

	private String orderId;

	private String amount;

	private String paymentKey;

}
