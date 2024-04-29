package cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.Image;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.QImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.QProductImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.QImage.*;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.QProductImage.productImage;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct.*;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.QStore.store;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.QUser.user;

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

    public List<ProductImage> getAllImage(Pageable pageable) {
        return jpaConfig.jpaQueryFactory()
                .selectFrom(productImage)
                .leftJoin(productImage.product, product).fetchJoin()
                .leftJoin(product.store, store).fetchJoin()
                .innerJoin(store.user, user).fetchJoin()
                .where(productImage.product.quantity.gt(1)
                        .and(productImage.product.deleted.eq(Deleted.UNDELETE)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<ProductImage> getAllImageWithSearchValue(Pageable pageable, String searchValue) {
        return jpaConfig.jpaQueryFactory()
                .selectFrom(productImage)
                .leftJoin(productImage.product, product).fetchJoin()
                .leftJoin(product.store, store).fetchJoin()
                .innerJoin(store.user, user).fetchJoin()
                .where(productImage.product.quantity.gt(1)
                        .and(productImage.product.deleted.eq(Deleted.UNDELETE))
                        .and(product.productName.contains(searchValue)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
