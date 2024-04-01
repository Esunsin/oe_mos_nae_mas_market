package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import lombok.Getter;

@Getter
public class ProductShowResponse {
    private String productName;
    private String info;
    private long real_price;
    private long price;
    private long discount;
    private long quantity;
    private String store;

    public ProductShowResponse(Product product) {
        this.productName = product.getProductName();
        this.info = product.getInfo();
        this.real_price = product.getReal_price();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.quantity = product.getQuantity();
        this.store = product.getStore().getStoreName();
    }

}
