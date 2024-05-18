package cheolppochwippo.oe_mos_nae_mas_market.domain.product;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository.ProductImageRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository.ProductImageRepositoryJdbc;
import cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.repoditory.InventoryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductByStoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class productServiceTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    StoreRepository storeRepository;
    @Mock
    ProductImageRepository productImageRepository;
    @Mock
    MessageSource messageSource;
    @Mock
    InventoryRepository inventoryRepository;
    @Mock
    ProductImageRepositoryJdbc productImageRepositoryJdbc;

    ProductService productService;

    @BeforeEach
    void before(){
        productService = new ProductServiceImpl(productRepository, storeRepository, productImageRepository, messageSource, inventoryRepository,productImageRepositoryJdbc);
    }

    @Test
    @DisplayName("상품생성 - jpa saveAll")
    void createProduct(){
        //given
        UserRequest userReq = new UserRequest("user1", "1234", "00", true);
        User seller = new User(userReq, "1234");
        seller.changeRoleToSeller();

        StoreRequest storeRequest = new StoreRequest("store1", "s");
        Store store = new Store(storeRequest, seller);

        List<String> urls = List.of("https://www.google.com", "https://www.baidu.com");
        ProductRequest productRequest = new ProductRequest("p", "p", 10000L, 1000L, 50L, urls);

        given(storeRepository.findByUserId(seller.getId())).willReturn(Optional.of(store));

        //when
        ProductResponse productResponse = productService.createProduct(productRequest, seller);

        //then
        assertEquals(productResponse.getProductId(),null);
    }
    @Test
    @DisplayName("상품생성 - jdbc bulk insert")
    void createProductBulkImage() throws SQLException {
        //given
        UserRequest userReq = new UserRequest("user1", "1234", "00", true);
        User seller = new User(userReq, "1234");
        seller.changeRoleToSeller();

        StoreRequest storeRequest = new StoreRequest("store1", "s");
        Store store = new Store(storeRequest, seller);

        List<String> urls = List.of("https://www.google.com", "https://www.baidu.com");
        ProductRequest productRequest = new ProductRequest("p", "p", 10000L, 1000L, 50L, urls);

        given(storeRepository.findByUserId(seller.getId())).willReturn(Optional.of(store));

        //when
        ProductResponse productResponse = productService.createProductBulkImage(productRequest, seller);

        //then
        assertEquals(productResponse.getProductId(),null);
    }
    @Test
    public void showProductByStore () throws Exception{
        //give
        UserRequest userReq = new UserRequest("user1", "1234", "00", true);
        User seller = new User(userReq, "1234");
        seller.changeRoleToSeller();

        StoreRequest storeRequest = new StoreRequest("store1", "s");
        Store store = new Store(storeRequest, seller);

        List<String> urls = List.of("https://www.google.com", "https://www.baidu.com");
        List<ProductRequest> productRequests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            productRequests.add(new ProductRequest("p", "p", 10000L, 1000L, 50L, urls));
        }

        List<Product> products = new ArrayList<>();
        for (ProductRequest productRequest : productRequests) {
            products.add(new Product(productRequest, store));
        }

        Pageable pageable = PageRequest.of(0,10);
        given(productRepository.findByStoreUserId(pageable, seller.getId())).willReturn(products);

        //when
        List<ProductByStoreResponse> productByStoreResponses = productService.showStoreProduct(pageable, seller);
        //then
        assertEquals(10,productByStoreResponses.size());
    }
}
