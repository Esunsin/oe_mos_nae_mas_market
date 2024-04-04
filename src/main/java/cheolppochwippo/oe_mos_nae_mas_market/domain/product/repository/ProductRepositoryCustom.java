package cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

public interface ProductRepositoryCustom {
    List<Product> findProductsWithQuantityGreaterThanOne(Pageable pageable);
}
