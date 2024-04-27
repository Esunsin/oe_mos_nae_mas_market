package cheolppochwippo.oe_mos_nae_mas_market.domain.search.document;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
@Setting(settingPath = "elasticsearch/product-setting.json")
@Mapping(mappingPath = "elasticsearch/product-mapping.json")
@Setter
public class ProductDocument {

    @Id
    private String id;
    private Long productId;
    private String productName;
    private String info;
    private Long price;
    private Long realPrice;
    private Long discount;
    private Long quantity;
    @Enumerated(EnumType.STRING)
    private Deleted deleted;

    public ProductDocument(String id, Long productId, String productName, String info,
        Long realPrice, Long discount, Long quantity) {
        this.id = UUID.randomUUID().toString();
        this.productId = productId;
        this.productName = productName;
        this.info = info;
        this.realPrice = realPrice;
        this.discount = discount;
        this.quantity = quantity;
        this.price = calculatePrice(realPrice, discount);
        this.deleted = Deleted.UNDELETE;
    }

    private Long calculatePrice(Long realPrice, Long discount) {
        return realPrice - discount;
    }

}
