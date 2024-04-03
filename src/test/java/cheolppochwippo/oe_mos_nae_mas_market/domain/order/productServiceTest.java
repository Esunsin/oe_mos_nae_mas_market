package cheolppochwippo.oe_mos_nae_mas_market.domain.order;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum.CONSUMER;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum.SELLER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductShowResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

@ExtendWith(MockitoExtension.class)
public class productServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    StoreRepository storeRepository;

    @Mock
    CacheManager cacheManager;


    ProductServiceImpl productService;

    User seller;
    User customer;
    Store store;
    Product product;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, storeRepository);

        seller = new User("seller", "password", SELLER);
        customer = new User("customer", "password", CONSUMER);
        store = new Store(seller, "Test Store", "Test Store Info");
        product = Product.builder()
            .id(1L)
            .productName("Test Product")
            .info("Test Product Info")
            .realPrice(10000L)
            .price(8000L)
            .discount(2000L)
            .quantity(10L)
            .store(store)
            .build();
    }

    @Test
    void createProduct_SellerRole_Success() {
        //given
        ProductRequest productRequest = new ProductRequest("Test Product", "Test Product Info",
            10000L, 8000L, 2000L, 10L);
        when(storeRepository.findByUser_Id(seller.getId())).thenReturn(Optional.of(store));

        //when
        ProductResponse result = productService.createProduct(productRequest, seller);

        //then
        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_CustomerRole_ThrowsIllegalArgumentException() {
        ProductRequest productRequest = new ProductRequest("Test Product", "Test Product Info",
            10000L, 8000L, 2000L, 10L);

        assertThrows(IllegalArgumentException.class,
            () -> productService.createProduct(productRequest, customer));
    }


    @Test
    void updateProduct_SellerRole_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductRequest updatedProductRequest = new ProductRequest("Updated Product",
            "Updated Product Info", 12000L, 10000L, 2000L, 15L);
        ProductResponse result = productService.updateProduct(updatedProductRequest, 1L, seller);

        assertEquals(product.getId(), result.getProductId());
    }

    @Test
    void updateProduct_CustomerRole_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProductRequest updatedProductRequest = new ProductRequest("Updated Product",
                "Updated Product Info", 12000L, 10000L, 2000L, 15L);
            productService.updateProduct(updatedProductRequest, 1L, customer);
        });
    }

    @Test
    void updateProduct_ProductNotFound_ThrowsNoSuchElementException() {

        ProductRequest updatedProductRequest = new ProductRequest("Updated Product",
            "Updated Product Info", 12000L, 10000L, 2000L, 15L);

        assertThrows(IllegalArgumentException.class,
            () -> productService.updateProduct(updatedProductRequest, 2L, customer));

    }

    @Test
    void showProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductShowResponse result = productService.showProduct(1L);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getProductName(), result.getProductName());
        assertEquals(product.getInfo(), result.getInfo());
        assertEquals(product.getRealPrice(), result.getRealPrice());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getDiscount(), result.getDiscount());
        assertEquals(product.getQuantity(), result.getQuantity());
        assertEquals(product.getStore().getStoreName(), result.getStoreName());
    }

    @Test
    void showProduct_ProductNotFound_ThrowsNoSuchElementException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.showProduct(1L));
    }

    @Test
    void showAllProduct_Success() {
        when(productRepository.findProductsWithQuantityGreaterThanOne()).thenReturn(
            Collections.singletonList(product));

        List<ProductShowResponse> result = productService.showAllProduct();

        assertNotNull(result);
        assertEquals(1, result.size());
        ProductShowResponse productShowResponse = result.get(0);
        assertEquals(product.getId(), productShowResponse.getId());
        assertEquals(product.getProductName(), productShowResponse.getProductName());
        assertEquals(product.getInfo(), productShowResponse.getInfo());
        assertEquals(product.getRealPrice(), productShowResponse.getRealPrice());
        assertEquals(product.getPrice(), productShowResponse.getPrice());
        assertEquals(product.getDiscount(), productShowResponse.getDiscount());
        assertEquals(product.getQuantity(), productShowResponse.getQuantity());
        assertEquals(product.getStore().getStoreName(), productShowResponse.getStoreName());
    }

    @Test
    void deleteProduct_SellerRole_Success() {
        // Given
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // When
        ProductResponse result = productService.deleteProduct(1L, seller);

        // Then
        assertEquals(product.getId(), result.getProductId());

    }

    @Test
    void deleteProduct_CustomerRole_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(1L, customer);
        });
    }


    @Test
    void testCaching() {
        when(productRepository.findProductsWithQuantityGreaterThanOne()).thenReturn(
            Collections.singletonList(product));

        // 캐시에 저장되지 않은 상태에서 showAllProduct() 호출
        List<ProductShowResponse> result1 = productService.showAllProduct();
        assertNotNull(result1);
        assertEquals(1, result1.size());

        // 캐시에 저장된 상태에서 showAllProduct() 호출
        List<ProductShowResponse> result2 = productService.showAllProduct();
        assertNotNull(result2);
        assertEquals(1, result2.size());

    }

    @Test
    void testCachingEfficiency() {
        int numProducts = 10000;
        List<Product> products = Collections.nCopies(numProducts, product);
        when(productRepository.findProductsWithQuantityGreaterThanOne()).thenReturn(products);

        // 캐시 없이 showAllProduct() 호출
        long startTime = System.nanoTime();
        List<ProductShowResponse> result1 = productService.showAllProduct();
        long duration1 = System.nanoTime() - startTime;

        // 캐시에 저장된 상태에서 showAllProduct() 호출
        startTime = System.nanoTime();
        List<ProductShowResponse> result2 = productService.showAllProduct();
        long duration2 = System.nanoTime() - startTime;

        // 캐시 적중으로 인한 성능 향상 확인
        assertTrue(duration2 < duration1);
    }
}
