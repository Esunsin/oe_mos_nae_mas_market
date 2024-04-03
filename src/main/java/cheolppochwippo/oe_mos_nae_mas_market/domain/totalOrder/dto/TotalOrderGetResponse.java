package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalOrderGetResponse {

	private Long id;

	private Long price;

	private Long discount;

	private Long priceAmount;

	private Long deliveryCost;

	private String orderName;

	private String address;

	private PaymentStatementEnum paymentStatementEnum;

	private String merchantUid;

	public TotalOrderGetResponse(TotalOrder totalOrder) {
		id = totalOrder.getId();
		price = totalOrder.getPrice();
		discount = totalOrder.getDiscount();
		priceAmount = totalOrder.getPriceAmount();
		deliveryCost = totalOrder.getDeliveryCost();
		orderName = totalOrder.getOrderName();
		address = totalOrder.getAddress();
		paymentStatementEnum = totalOrder.getPaymentStatementEnum();
		merchantUid = totalOrder.getMerchantUid();
	}

}
