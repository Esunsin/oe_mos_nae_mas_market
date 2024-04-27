package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductUpdateRequest {

	private String productName;
	private String info;
	private long realPrice;
	private long discount;
}
