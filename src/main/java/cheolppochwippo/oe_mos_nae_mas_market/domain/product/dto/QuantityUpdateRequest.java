package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuantityUpdateRequest {
    private Long productId;
    private Long quantity;

}
