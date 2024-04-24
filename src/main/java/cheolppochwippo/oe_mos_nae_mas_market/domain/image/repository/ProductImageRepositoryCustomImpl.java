package cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.Image;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.QImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.QProductImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.QImage.*;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.QProductImage.productImage;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct.*;

@RequiredArgsConstructor
public class ProductImageRepositoryCustomImpl implements ProductImageRepositoryCustom{

    private final JpaConfig jpaConfig;

    public List<ProductImage> getImageByProductId(Long productId) {
        return jpaConfig.jpaQueryFactory()
                .selectFrom(productImage)
                .innerJoin(productImage.product,product)
                .where(product.id.eq(productId)
                        .and(product.deleted.eq(Deleted.UNDELETE)))
                .fetch();
    }
}
