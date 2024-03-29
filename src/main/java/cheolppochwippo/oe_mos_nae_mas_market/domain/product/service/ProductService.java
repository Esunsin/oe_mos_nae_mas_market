package cheolppochwippo.oe_mos_nae_mas_market.domain.product.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import org.springframework.stereotype.Service;


public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse showProduct(ProductRequest productRequest);

    ProductResponse updateProduct(ProductRequest productRequest);

    ProductResponse showAllProduct(ProductRequest productRequest);

    ProductResponse deleteProduct(ProductRequest productRequest);
}
