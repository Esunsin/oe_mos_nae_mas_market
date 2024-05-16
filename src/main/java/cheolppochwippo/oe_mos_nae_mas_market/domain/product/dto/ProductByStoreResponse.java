package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductByStoreResponse {
    private Long id;
    private String productName;
    private String info;
    private Long realPrice;
    private Long price;
    private Long discount;

    public ProductByStoreResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.info = product.getInfo();
        this.realPrice = product.getRealPrice();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
    }
}
