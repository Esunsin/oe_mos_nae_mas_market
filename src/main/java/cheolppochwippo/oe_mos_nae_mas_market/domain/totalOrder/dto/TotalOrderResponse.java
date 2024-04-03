package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.DeliveryStatus;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalOrderResponse {

	private Long totalOrderId;

	private Long price;

	private Long discount;

	private Long priceAmount;

	private Long deliveryCost;

	private String username;

	private String orderName;

	private String address;

	private DeliveryStatus deliveryStatus;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

	public TotalOrderResponse(TotalOrder totalOrder) {
		this.totalOrderId = totalOrder.getId();
		this.price = totalOrder.getPrice();
		this.discount = totalOrder.getDiscount();
		this.priceAmount = totalOrder.getPriceAmount();
		this.deliveryCost = totalOrder.getDeliveryCost();
		this.username = totalOrder.getUser().getUsername();
		this.orderName = totalOrder.getOrderName();
		this.address = totalOrder.getAddress();
		this.createdAt = totalOrder.getCreatedAt();
		this.modifiedAt = totalOrder.getModifiedAt();
	}
}
