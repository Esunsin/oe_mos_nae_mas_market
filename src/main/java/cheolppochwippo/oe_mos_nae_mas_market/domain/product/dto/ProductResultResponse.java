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

    public ProductResultResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.info = product.getInfo();
        this.realPrice = product.getRealPrice();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.storeName = product.getStore().getStoreName();
        this.storeInfo = product.getStore().getInfo();
    }

    public void setProductResultResponse(ProductImage image) {
        this.id = image.getProduct().getId();
        this.productName = image.getProduct().getProductName();
        this.info = image.getProduct().getInfo();
        this.realPrice = image.getProduct().getRealPrice();
        this.price = image.getProduct().getPrice();
        this.discount = image.getProduct().getDiscount();
        this.storeName = image.getProduct().getStore().getStoreName();
        this.storeInfo = image.getProduct().getStore().getInfo();
    }

    public void addImageUrl(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }
}
