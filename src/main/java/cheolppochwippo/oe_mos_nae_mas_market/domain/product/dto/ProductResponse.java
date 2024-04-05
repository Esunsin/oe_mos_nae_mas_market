package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse {
    private Long productId;
    public ProductResponse(Product product) {
        this.productId = product.getId();
    }
}
