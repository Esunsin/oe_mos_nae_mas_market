package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TotalOrderResponse {
	private Long amount;
	private String orderName;
	private String orderId;
	private String successUrl;
	private String failUrl;

	public TotalOrderResponse(TotalOrder totalOrder){
		amount = totalOrder.getPriceAmount();
		orderName = totalOrder.getOrderName();
		orderId = totalOrder.getMerchantUid();
	}
}
