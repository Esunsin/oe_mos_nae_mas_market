package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductShowResponse {

    private List<ProductResultResponse> productList;

    public ProductShowResponse(List<ProductResultResponse> productList) {
        this.productList = productList;
    }
}
