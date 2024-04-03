package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import lombok.Getter;

@Getter
public class ProductShowResponse {
    private Long id;
    private String productName;
    private String info;
    private long real_price;
    private long price;
    private long discount;
    private long quantity;
    private String storeName;

    public ProductShowResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.info = product.getInfo();
        this.real_price = product.getRealPrice();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.quantity = product.getQuantity();
        this.storeName = product.getStore().getStoreName();
    }

}
