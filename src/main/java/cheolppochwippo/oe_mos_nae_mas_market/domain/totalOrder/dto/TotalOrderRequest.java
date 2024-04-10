package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TotalOrderRequest {

	private Long issuedId = 0L;

	private String address;

}
