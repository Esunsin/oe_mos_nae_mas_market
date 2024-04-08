package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCancelRequest {

	private String paymentKey;

	private String cancelReason;
}
