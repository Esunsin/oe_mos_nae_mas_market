package cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct.product;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.QStore.store;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.QUser.user;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.QUser;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import com.querydsl.core.QueryResults;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JpaConfig jpaConfig;


    @Override
    public List<Product> findProductsWithQuantityGreaterThanOne(Pageable pageable) {
        QueryResults<Product> queryResults = jpaConfig.jpaQueryFactory()

                .selectFrom(product)
                .innerJoin(product.store,store).fetchJoin()
                .innerJoin(store.user, user).fetchJoin()
                .where(product.quantity.gt(1)
                .and(product.deleted.eq(Deleted.UNDELETE)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<Product> content = queryResults.getResults();
        return content;

    }

    @Override
    public Product findByOrder(Order order) {
        Product query = jpaConfig.jpaQueryFactory()
            .select(product)
            .from(product)
            .where(
                product.id.eq(order.getProduct().getId())
            )
            .fetchOne();
        return query;
    }

    public Optional<Product> findProductById(Long id) {
        Product result = jpaConfig.jpaQueryFactory()
                .selectFrom(product)
                .leftJoin(product.store, store).fetchJoin()
                .where(product.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    public List<Product> findProductsWithQuantityGreaterThanOneAndSearchValue(Pageable pageable,String searchValue) {
        return jpaConfig.jpaQueryFactory()
                .selectFrom(product)
                .innerJoin(product.store,store).fetchJoin()
                .innerJoin(store.user, user).fetchJoin()
                .where(product.quantity.gt(1)
                        .and(product.deleted.eq(Deleted.UNDELETE))
                        .and(product.productName.contains(searchValue)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Product> findByStoreUserId(Pageable pageable, Long userId) {
        return jpaConfig.jpaQueryFactory()
            .selectFrom(product)
            .innerJoin(product.store, store)
            .innerJoin(store.user, user)
            .where(user.id.eq(userId)
                .and(product.deleted.eq(Deleted.UNDELETE)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public Optional<Product> findByproductIdAndUserId(Long userId, Long productId) {
        return Optional.ofNullable(jpaConfig.jpaQueryFactory()
			.selectFrom(product)
			.where(
				product.store.user.id.eq(userId),
				product.id.eq(productId)
			).fetchOne());
    }
}
