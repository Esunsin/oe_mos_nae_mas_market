package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalOrderRequest {

	private Long amount;

	private String orderName;

	private Long couponId;

}
