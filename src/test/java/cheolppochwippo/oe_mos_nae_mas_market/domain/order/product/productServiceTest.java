package cheolppochwippo.oe_mos_nae_mas_market.domain.order.product;

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
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResultResponse;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class productServiceTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    RedissonClient redissonClient;
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
        productService = new ProductServiceImpl(productRepository, storeRepository,redissonClient);

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
    @DisplayName("상품 생성_성공")
    void createProduct_SellerRole_Success() {
        //given
        ProductRequest productRequest = new ProductRequest("Test Product", "Test Product Info",
            10000L, 8000L, 2000L, 10L);
        given(storeRepository.findByUser_Id(seller.getId())).willReturn(Optional.of(store));

        //when
        ProductResponse result = productService.createProduct(productRequest, seller);

        //then
        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 생성_seller 아닐때_실패")
    void createProduct_CustomerRole_ThrowsIllegalArgumentException() {
        ProductRequest productRequest = new ProductRequest("Test Product", "Test Product Info",
            10000L, 8000L, 2000L, 10L);

        assertThrows(IllegalArgumentException.class,
            () -> productService.createProduct(productRequest, customer));
    }


    @Test
    @DisplayName("상품 수정_성공")
    void updateProduct_SellerRole_Success() {
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        ProductRequest updatedProductRequest = new ProductRequest("Updated Product",
            "Updated Product Info", 12000L, 10000L, 2000L, 15L);
        ProductResponse result = productService.updateProduct(updatedProductRequest, 1L, seller);

        assertEquals(product.getId(), result.getProductId());
    }

    @Test
    @DisplayName("상품 수정_SELLER 아닐때_실패")
    void updateProduct_CustomerRole_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProductRequest updatedProductRequest = new ProductRequest("Updated Product",
                "Updated Product Info", 12000L, 10000L, 2000L, 15L);
            productService.updateProduct(updatedProductRequest, 1L, customer);
        });
    }

    @Test
    @DisplayName("상품 수정_존재하지않는 상품_실패")
    void updateProduct_ProductNotFound_ThrowsNoSuchElementException() {

        ProductRequest updatedProductRequest = new ProductRequest("Updated Product",
            "Updated Product Info", 12000L, 10000L, 2000L, 15L);

        assertThrows(IllegalArgumentException.class,
            () -> productService.updateProduct(updatedProductRequest, 2L, customer));

    }

    @Test
    @DisplayName("상품 단건 조회_성공")
    void showProduct_Success() {
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        ProductResultResponse result = productService.showProduct(1L);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getProductName(), result.getProductName());
        assertEquals(product.getInfo(), result.getInfo());
        assertEquals(product.getRealPrice(), result.getRealPrice());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getDiscount(), result.getDiscount());
        assertEquals(product.getQuantity(), result.getQuantity());
        assertEquals(product.getStore().getStoreName(), result.getStore().getStoreName());
    }

    @Test
    @DisplayName("상품 단건조회_실패")
    void showProduct_ProductNotFound_ThrowsNoSuchElementException() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.showProduct(2L));
    }

    @Test
    @DisplayName("상품 전체 조회_성공")
    void showAllProduct_Success() {

        Pageable pageable = PageRequest.of(0, 10);
        List<Product> productList = Collections.singletonList(product);
        when(productRepository.findProductsWithQuantityGreaterThanOne(pageable)).thenReturn(
            productList);

        ProductShowResponse result = productService.showAllProduct(pageable);

        assertEquals(1, result.getProductList().size());

    }

    @Test
    @DisplayName("상품 삭제_성공")
    void deleteProduct_SellerRole_Success() {
        // Given
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // When
        ProductResponse result = productService.deleteProduct(1L, seller);

        // Then
        assertEquals(product.getId(), result.getProductId());

    }

    @Test
    @DisplayName("상품 삭제_실패")
    void deleteProduct_CustomerRole_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(1L, customer);
        });
    }

    @Test
    @DisplayName("캐싱_성공")
    void testCaching() {

        Pageable pageable = PageRequest.of(0, 10);
        List<Product> productList = Collections.singletonList(product);
        when(productRepository.findProductsWithQuantityGreaterThanOne(pageable)).thenReturn(
            productList);

        // 캐시에 저장되지 않은 상태에서 showAllProduct() 호출
        ProductShowResponse result1 = productService.showAllProduct(pageable);
        assertNotNull(result1);

        // 캐시에 저장된 상태에서 showAllProduct() 호출
        ProductShowResponse result2 = productService.showAllProduct(pageable);
        assertNotNull(result2);

        for (int i = 0; i < result1.getProductList().size(); i++) {
            ProductResultResponse product1 = result1.getProductList().get(i);
            ProductResultResponse product2 = result2.getProductList().get(i);
            assertEquals(product1.getId(), product2.getId());
            assertEquals(product1.getProductName(), product2.getProductName());
        }
    }

    @Test
    @DisplayName("캐싱 성능 비교")
    void testCachingEfficiency() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> productList = Collections.singletonList(product);
        when(productRepository.findProductsWithQuantityGreaterThanOne(pageable)).thenReturn(
            productList);

        long startTime = System.nanoTime();
        ProductShowResponse result1 = productService.showAllProduct(pageable);
        long duration1 = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        ProductShowResponse result2 = productService.showAllProduct(pageable);
        long duration2 = System.nanoTime() - startTime;

        // 캐싱으로 인한 성능 향상 확인
        System.out.println("캐싱 전 " + duration1);
        System.out.println("캐싱 후 " + duration2);
        assertTrue(duration2 < duration1);
    }
}
