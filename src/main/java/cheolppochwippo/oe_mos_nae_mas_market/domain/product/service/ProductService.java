package cheolppochwippo.oe_mos_nae_mas_market.domain.product.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.*;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;


public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest, User user);

    ProductResultResponse showProduct(long productId);

    ProductResponse updateProduct(ProductUpdateRequest productRequest, Long productId, User user);

    ProductShowResponse showAllProduct(Pageable pageable);

    ProductResponse deleteProduct(Long productId, User user);

    ProductShowResponse showAllProductWithValue(Pageable pageable, String searchValue);

    List<ProductByStoreResponse> showStoreProduct(Pageable pageable, User user);

    ProductResponse updateQuantity(QuantityUpdateRequest productRequest, User user);

    ProductResponse createProductBulkImage(ProductRequest productRequest, User user) throws SQLException;
}
