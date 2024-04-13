package cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity;

import cheolppochwippo.oe_mos_nae_mas_market.global.entity.Image;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("P")
public class ProductImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

}
