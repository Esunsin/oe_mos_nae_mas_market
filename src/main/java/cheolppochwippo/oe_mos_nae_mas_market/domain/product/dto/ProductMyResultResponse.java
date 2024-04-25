package cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductMyResultResponse {

	private Long id;
	private String productName;
	private String info;
	private Long realPrice;
	private Long price;
	private Long discount;
	private String storeName;
	private String storeInfo;
	private Long quantity;
	private final List<String> imageUrls = new ArrayList<>();

	public ProductMyResultResponse(Product product, List<ProductImage> productImages) {
		this.id = product.getId();
		this.productName = product.getProductName();
		this.info = product.getInfo();
		this.realPrice = product.getRealPrice();
		this.price = product.getPrice();
		this.discount = product.getDiscount();
		this.storeName = product.getStore().getStoreName();
		this.storeInfo = product.getStore().getInfo();
		this.quantity = product.getQuantity();
		for (ProductImage productImage : productImages) {
			this.imageUrls.add(productImage.getUrl());
		}
	}

}
