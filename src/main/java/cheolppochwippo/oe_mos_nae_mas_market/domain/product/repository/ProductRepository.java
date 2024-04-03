package cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>,ProductRepositoryCustom {


}
