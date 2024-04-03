package cheolppochwippo.oe_mos_nae_mas_market.domain.product.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductShowResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest, User user) {
        if (RoleEnum.SELLER.equals(user.getRole())) {

            User seller = new User(user.getId(), user.getUsername(),
                String.valueOf(user.getRole()));

            Store store = storeRepository.findByUser_Id(seller.getId());
            Product product = new Product(productRequest, store);

            productRepository.save(product);

            return new ProductResponse(product.getId());
        } else {
            throw new IllegalArgumentException("판매자만 상품을 등록할 수 있습니다.");
        }
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequest productRequest, Long productId, User user) {
        if (RoleEnum.SELLER.equals(user.getRole())) {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다."));
            product.update(productRequest);
            return new ProductResponse(product.getId());
        } else {
            throw new IllegalArgumentException("판매자만 상품을 등록할 수 있습니다.");
        }
    }


    @Override
    public ProductShowResponse showProduct(long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다."));

        return new ProductShowResponse(product);
    }


    @Override
    public List<ProductShowResponse> showAllProduct() {
        List<Product> productList = productRepository.findAll();
        List<ProductShowResponse> productShowResponseList = productList.stream()
            .map(ProductShowResponse::new)
            .collect(Collectors.toList());
        return productShowResponseList;
    }

    @Override
    public ProductResponse deleteProduct(Long productId, User user) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다."));
        productRepository.delete(product);
        return new ProductResponse(product.getId());
    }
}