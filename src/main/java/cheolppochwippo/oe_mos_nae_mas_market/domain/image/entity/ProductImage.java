package cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("P")
public class ProductImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

}
