package cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SingleOrderInCartResponse {

	private Long orderId;

	private String productName;

	private Long quantity;

	private Long price;

	private Deleted deleted;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

	public SingleOrderInCartResponse(Order order) {
		this.orderId = order.getId();
		this.productName = order.getProduct().getProductName();
		this.quantity = order.getQuantity();
		this.price = order.getPrice();
		this.deleted = order.getDeleted();
		this.createdAt = order.getCreatedAt();
		this.modifiedAt = order.getModifiedAt();
	}
}

