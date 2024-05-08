package cheolppochwippo.oe_mos_nae_mas_market.domain.product.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository.ProductImageRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.entity.Inventory;
import cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.repoditory.InventoryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductMyResultResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResultResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductShowResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.QuantityUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final ProductImageRepository productImageRepository;
    private final MessageSource messageSource;
    private final CacheManager cacheManager;
    private final InventoryRepository inventoryRepository;

    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true) // 기능 만들어야 됨
    public ProductResponse createProduct(ProductRequest productRequest, User user) {
        validateSeller(user);
        Store store = storeRepository.findByUserId(user.getId())
            .orElseThrow(() -> new NoSuchElementException(
                messageSource.getMessage("noSuch.store", null, Locale.KOREA)));

        Product product = new Product(productRequest, store);
        Product saveProduct = productRepository.save(product);

        List<String> imageUrls = getProductImageUrls(saveProduct.getId());

        return new ProductResponse(product);
    }

    @Override
    public ProductShowResponse showStoreProduct(Pageable pageable, User user) {
        validateSeller(user);
        List<Product> productList = productRepository.findByStoreUserId(pageable, user.getId());
        List<ProductResultResponse> productResultResponseList = new ArrayList<>();
        for (Product product : productList) {
            List<ProductImage> imageByProductId = productImageRepository.getImageByProductId(
                product.getId());
            productResultResponseList.add(new ProductResultResponse(product, imageByProductId));
        }
        return new ProductShowResponse(productResultResponseList);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true) // 기능
    public ProductResponse updateProduct(ProductUpdateRequest productRequest, Long productId,
        User user) {
        validateSeller(user);
        Product product = foundProduct(productId);
        product.update(productRequest);
        List<ProductImage> imageByProductId = productImageRepository.getImageByProductId(productId);
        ProductResultResponse response = new ProductResultResponse(product, imageByProductId);
        Objects.requireNonNull(cacheManager.getCache("product")).put(productId, response);

        List<String> imageUrls = getProductImageUrls(productId);

        return new ProductResponse(product);
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "product", key = "#productId")
    public ProductResultResponse showProduct(long productId) {
        Product product = foundProduct(productId);
        List<ProductImage> imageByProductId = productImageRepository.getImageByProductId(productId);
        return new ProductResultResponse(product, imageByProductId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductMyResultResponse showMyProduct(Long userId, long productId) {
        Product product = productRepository.findByproductIdAndUserId(userId, productId)
            .orElseThrow(() -> new NoSuchElementException(
                messageSource.getMessage("noEntity.product", null, Locale.KOREA)));
        List<ProductImage> imageByProductId = productImageRepository.getImageByProductId(productId);
        return new ProductMyResultResponse(product, imageByProductId);
    }

    @Override
    @Transactional
    public ProductResponse updateQuantity(QuantityUpdateRequest productRequest,
        User user) {
        Product product = productRepository.findByproductIdAndUserId(user.getId(),
                productRequest.getProductId())
            .orElseThrow(() -> new NoSuchElementException(
                messageSource.getMessage("noEntity.product", null, Locale.KOREA)));
        Inventory inventory = new Inventory(productRequest, product);
        inventoryRepository.save(inventory);
        return new ProductResponse(inventory);
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "products", key = "#pageable")
    public ProductShowResponse showAllProduct(Pageable pageable) {
        List<ProductImage> image = productImageRepository.getAllImage(pageable);
        List<ProductResultResponse> productResultResponseList = new ArrayList<>();
        List<Long> productIds = new ArrayList<>();
        for (ProductImage productImage : image) {
            ProductResultResponse productResultResponse = new ProductResultResponse();
            if(productIds.contains(productImage.getProduct().getId())){
                for (ProductResultResponse resultResponse :productResultResponseList) {
                    if(resultResponse.getId().equals(productImage.getProduct().getId())){
                        resultResponse.addImageUrl(productImage.getUrl());
                        break;
                    }

                }
            }
            else {
                productIds.add(productImage.getProduct().getId());
                productResultResponse.setProductResultResponse(productImage);
                productResultResponse.addImageUrl(productImage.getUrl());
                productResultResponseList.add(productResultResponse);
            }
        }
        return new ProductShowResponse(productResultResponseList);
    }

    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)//기능
    public ProductResponse deleteProduct(Long productId, User user) {
        validateSeller(user);

        Product product = foundProduct(productId);
        product.delete();

        return new ProductResponse(product);
    }


    private Product foundProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException(
                messageSource.getMessage("noEntity.product", null, Locale.KOREA)));
    }

    private void validateSeller(User user) {
        if (!RoleEnum.SELLER.equals(user.getRole())) {
            throw new NoPermissionException(
                messageSource.getMessage("noPermission.role.seller", null, Locale.KOREA));
        }
    }

    @Transactional(readOnly = true)
    public ProductShowResponse showAllProductWithValue(Pageable pageable, String searchValue) {
        List<ProductImage> image;
        if (searchValue == null) {
            log.info("없을때");
            image = productImageRepository.getAllImage(pageable);
        } else {
            log.info("있을때");
            image = productImageRepository.getAllImageWithSearchValue(pageable,searchValue);
        }

        List<ProductResultResponse> productResultResponseList = new ArrayList<>();
        List<Long> productIds = new ArrayList<>();
        for (ProductImage productImage : image) {
            ProductResultResponse productResultResponse = new ProductResultResponse();
            if(productIds.contains(productImage.getProduct().getId())){
                for (ProductResultResponse resultResponse :productResultResponseList) {
                    if(resultResponse.getId().equals(productImage.getProduct().getId())){
                        resultResponse.addImageUrl(productImage.getUrl());
                        break;
                    }

                }
            }
            else {
                productIds.add(productImage.getProduct().getId());
                productResultResponse.setProductResultResponse(productImage);
                productResultResponse.addImageUrl(productImage.getUrl());
                productResultResponseList.add(productResultResponse);
            }
        }
        return new ProductShowResponse(productResultResponseList);
    }

    private List<String> getProductImageUrls(Long productId) {
        List<ProductImage> productImages = productImageRepository.getImageByProductId(productId);
        return productImages.stream()
            .map(ProductImage::getUrl)
            .collect(Collectors.toList());
    }

}
