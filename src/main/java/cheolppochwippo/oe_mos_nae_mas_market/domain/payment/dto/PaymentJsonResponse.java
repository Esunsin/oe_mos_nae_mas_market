package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
@AllArgsConstructor
public class PaymentJsonResponse {

	private JSONObject jsonObject;

	private int code;

}
