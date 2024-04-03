package cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct.product;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.QStore.store;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{
    private final JpaConfig jpaConfig;
    @Override
    public List<Product> findProductsWithQuantityGreaterThanOne() {
        return jpaConfig.jpaQueryFactory()
            .selectFrom(product)
            .innerJoin(product.store, store)
            .where(product.quantity.gt(1)
                .and(product.deleted.eq(Deleted.UNDELETE)))
            .fetch();
    }
}
