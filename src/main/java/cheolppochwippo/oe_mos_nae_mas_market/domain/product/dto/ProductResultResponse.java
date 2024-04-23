package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.Image;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductResultResponse {

    private Long id;
    private String productName;
    private String info;
    private Long realPrice;
    private Long price;
    private Long discount;
    private String storeName;
    private String storeInfo;
    private final List<String> imageUrls = new ArrayList<>();

    public ProductResultResponse(Product product, List<Image> productImages) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.info = product.getInfo();
        this.realPrice = product.getRealPrice();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.storeName = product.getStore().getStoreName();
        this.storeInfo = product.getStore().getInfo();
        for (Image productImage : productImages) {
            this.imageUrls.add(productImage.getUrl());
        }
    }
}
