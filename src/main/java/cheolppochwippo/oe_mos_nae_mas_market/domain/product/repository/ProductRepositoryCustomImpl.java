package cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct.product;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.QStore.store;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import com.querydsl.core.QueryResults;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JpaConfig jpaConfig;


    @Override
    public List<Product> findProductsWithQuantityGreaterThanOne(Pageable pageable) {
        QueryResults<Product> queryResults = jpaConfig.jpaQueryFactory()
            .selectFrom(product)
            .innerJoin(product.store, store)
            .where(product.quantity.gt(1)
                .and(product.deleted.eq(Deleted.UNDELETE)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<Product> content = queryResults.getResults();
        return content;

    }
}
