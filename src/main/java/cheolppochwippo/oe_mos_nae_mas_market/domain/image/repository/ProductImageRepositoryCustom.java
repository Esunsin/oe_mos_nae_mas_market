package cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.Image;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductImageRepositoryCustom {

    List<ProductImage> getImageByProductId(Long productId);

    List<ProductImage> getAllImage(Pageable pageable);

    List<ProductImage> getAllImageWithSearchValue(Pageable pageable, String searchValue);
}
