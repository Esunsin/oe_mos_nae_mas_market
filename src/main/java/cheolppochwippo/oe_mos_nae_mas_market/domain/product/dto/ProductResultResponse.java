package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResultResponse {

    private Long id;
    private String productName;
    private String info;
    private Long realPrice;
    private Long price;
    private Long discount;
    //private Long quantity;
    private StoreResponse store;

    public ProductResultResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.info = product.getInfo();
        this.realPrice = product.getRealPrice();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
       // this.quantity = product.getQuantity();
        this.store = new StoreResponse(product.getStore());
    }
}
