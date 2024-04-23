package cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.Image;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;

import java.util.List;

public interface ProductImageRepositoryCustom {

    List<Image> getImageByProductId(Long productId);
}
