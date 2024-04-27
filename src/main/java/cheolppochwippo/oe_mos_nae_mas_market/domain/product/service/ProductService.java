package cheolppochwippo.oe_mos_nae_mas_market.domain.product.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductMyResultResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResultResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductShowResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


public interface ProductService {

    @Transactional
    ProductResponse createProduct(ProductRequest productRequest, User user);

    ProductResultResponse showProduct(long productId);

    @Transactional
    ProductResponse updateProduct(ProductUpdateRequest productRequest, Long productId, User user);

    ProductShowResponse showAllProduct(Pageable pageable);

    ProductResponse deleteProduct(Long productId, User user);

    ProductShowResponse showAllProductWithValue(Pageable pageable, String searchValue);

    ProductShowResponse showStoreProduct(Pageable pageable, User user);

    ProductMyResultResponse showMyProduct(Long userId, long productId);
}
