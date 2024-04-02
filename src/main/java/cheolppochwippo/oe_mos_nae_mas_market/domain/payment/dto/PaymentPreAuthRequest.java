package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentPreAuthRequest {

	private String merchant_uid;

	private String amount;
}
