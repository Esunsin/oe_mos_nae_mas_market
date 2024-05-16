package cheolppochwippo.oe_mos_nae_mas_market.domain.product.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository.ProductImageRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository.ProductImageRepositoryJdbc;
import cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.entity.Inventory;
import cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.repoditory.InventoryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.*;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;

import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final InventoryRepository inventoryRepository;
    private final ProductImageRepositoryJdbc productImageRepositoryJdbc;

    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductResponse createProduct(ProductRequest productRequest, User user) {
        User seller = validateSeller(user);

        Store store = storeRepository.findByUserId(seller.getId())
            .orElseThrow(() -> new NoSuchElementException(
                messageSource.getMessage("noSuch.store", null, Locale.KOREA)));

        Product product = new Product(productRequest, store);
        Product saveProduct = productRepository.save(product);

        List<ProductImage> productImages = new ArrayList<>();
        for (String url : productRequest.getImageUrl()) {
            productImages.add(new ProductImage(url, saveProduct));
        }

        productImageRepository.saveAll(productImages);

        return new ProductResponse(product);
    }

    @Transactional
    public ProductResponse createProductBulkImage(ProductRequest productRequest, User user) {
        User seller = validateSeller(user);

        Store store = storeRepository.findByUserId(seller.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        messageSource.getMessage("noSuch.store", null, Locale.KOREA)));

        Product product = new Product(productRequest, store);
        Product saveProduct = productRepository.save(product);

        List<ProductImage> productImages = new ArrayList<>();
        for (String url : productRequest.getImageUrl()) {
            productImages.add(new ProductImage(url, saveProduct));
        }

        productImageRepositoryJdbc.productImageBulkInsert(productImages);

        return new ProductResponse(product);
    }

    @Override
    public List<ProductByStoreResponse> showStoreProduct(Pageable pageable, User user) {
        User seller = validateSeller(user);
        List<Product> products = productRepository.findByStoreUserId(pageable, seller.getId());
        List<ProductByStoreResponse> result = new ArrayList<>();
        for (Product product : products) {
            result.add(new ProductByStoreResponse(product));
        }
        return result;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductResponse updateProduct(ProductUpdateRequest productRequest, Long productId,
        User user) {
        validateSeller(user);
        Product product = foundProduct(productId);
        product.update(productRequest);
        return new ProductResponse(product);
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "product", key = "#productId")
    public ProductResultResponse showProduct(long productId) {
        List<ProductImage> productImages = productImageRepository.getImageByProductId(productId);
        ProductResultResponse productResultResponse = new ProductResultResponse();
        productResultResponse.setProductResultResponse(productImages.get(0));
        for (ProductImage productImage : productImages) {
            productResultResponse.addImageUrl(productImage.getUrl());
        }
        return productResultResponse;
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
        Map<Product, List<String>> productAndUrlMap = new HashMap<>();

        for (ProductImage productImage : image) {
            if(!productAndUrlMap.containsKey(productImage.getProduct())) {
                productAndUrlMap.put(productImage.getProduct()
                        , productAndUrlMap.getOrDefault(productImage.getProduct(), new ArrayList<>()));
            }
            productAndUrlMap.get(productImage.getProduct()).add(productImage.getUrl());
        }

        for (Product product : productAndUrlMap.keySet()) {
            ProductResultResponse productResultResponse = new ProductResultResponse(product);
            for (String url : productAndUrlMap.get(product)) {
                productResultResponse.addImageUrl(url);
            }
            productResultResponseList.add(productResultResponse);
        }

        return new ProductShowResponse(productResultResponseList);
    }

    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductResponse deleteProduct(Long productId, User user) {
        validateSeller(user);

        Product product = foundProduct(productId);
        product.delete();

        return new ProductResponse(product);
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
        Map<Product, List<String>> productAndUrlMap = new HashMap<>();

        for (ProductImage productImage : image) {
            if(productAndUrlMap.containsKey(productImage.getProduct())) {
                productAndUrlMap.put(productImage.getProduct()
                        , productAndUrlMap.getOrDefault(productImage.getProduct(), new ArrayList<>()));
            }
            productAndUrlMap.get(productImage.getProduct()).add(productImage.getUrl());
        }

        for (Product product : productAndUrlMap.keySet()) {
            ProductResultResponse productResultResponse = new ProductResultResponse(product);
            for (String url : productAndUrlMap.get(product)) {
                productResultResponse.addImageUrl(url);
            }
            productResultResponseList.add(productResultResponse);
        }

        return new ProductShowResponse(productResultResponseList);
    }

    private Product foundProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException(
                messageSource.getMessage("noEntity.product", null, Locale.KOREA)));
    }

    private User validateSeller(User user) {
        if (!RoleEnum.SELLER.equals(user.getRole())) {
            throw new NoPermissionException(
                messageSource.getMessage("noPermission.role.seller", null, Locale.KOREA));
        }
        return user;
    }

}
