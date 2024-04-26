package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
@AllArgsConstructor
public class PaymentSuccessResponse {

	private JSONObject jsonObject;

	private int code;

	private String address;
}
