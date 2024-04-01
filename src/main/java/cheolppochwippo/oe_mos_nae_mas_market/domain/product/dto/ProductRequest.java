package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductRequest {

    private String productName;
    private String info;
    private long real_price;
    private long price;
    private long discount;
    private long quantity;

}
