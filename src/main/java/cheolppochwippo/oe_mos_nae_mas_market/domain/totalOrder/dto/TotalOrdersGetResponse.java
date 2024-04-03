package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalOrdersGetResponse {

	private Long id;

	private Long priceAmount;

	private String orderName;

	private PaymentStatementEnum paymentStatementEnum;

	private String merchantUid;

	public TotalOrdersGetResponse(TotalOrder totalOrder) {
		id = totalOrder.getId();
		priceAmount = totalOrder.getPriceAmount();
		orderName = totalOrder.getOrderName();
		paymentStatementEnum = totalOrder.getPaymentStatementEnum();
		merchantUid = totalOrder.getMerchantUid();
	}
}
