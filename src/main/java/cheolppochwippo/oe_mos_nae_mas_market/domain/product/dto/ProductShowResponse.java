package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductShowResponse {
    private Long id;
    private String productName;
    private String info;
    private long realPrice;
    private long price;
    private long discount;
    private long quantity;
    private String storeName;

    public ProductShowResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.info = product.getInfo();
        this.realPrice = product.getRealPrice();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.quantity = product.getQuantity();
        this.storeName = product.getStore().getStoreName();
    }

}
