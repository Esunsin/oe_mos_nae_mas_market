package cheolppochwippo.oe_mos_nae_mas_market.domain.product.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductShowResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface ProductService {
    @Transactional
    ProductResponse createProduct(ProductRequest productRequest, User user);

    ProductShowResponse showProduct(long productId);
    @Transactional
    ProductResponse updateProduct(ProductRequest productRequest,Long productId,User user);

    List<ProductShowResponse> showAllProduct();

    ProductResponse deleteProduct(Long productId, User user);
}
