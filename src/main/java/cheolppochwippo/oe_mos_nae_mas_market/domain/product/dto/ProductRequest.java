package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProductRequest {

    private String productName;
    private String info;
    private long realPrice;
    private long discount;
    private long quantity;
    private List<String> imageUrl;
}
