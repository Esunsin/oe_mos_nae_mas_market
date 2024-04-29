package cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.OrderStatementEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllOrderInStoreResponse {

	private Long orderId;

	private String productName;

	private Long quantity;

	private Long price;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

	private Long totalPrice;

	private OrderStatementEnum statement;

}
