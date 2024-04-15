package cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    List<Product> findProductsWithQuantityGreaterThanOne(Pageable pageable);

    Product findByOrder(Order order);

    List<Product> findProductsWithQuantityGreaterThanOneAndSearchValue(Pageable pageable,String searchValue);
}
