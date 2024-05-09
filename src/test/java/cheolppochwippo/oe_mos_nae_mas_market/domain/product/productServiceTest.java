package cheolppochwippo.oe_mos_nae_mas_market.domain.product;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository.ProductImageRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.repoditory.InventoryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
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
    CacheManager cacheManager;
    @Mock
    InventoryRepository inventoryRepository;

    ProductService productService;

    @BeforeEach
    void before(){
        productService = new ProductServiceImpl(productRepository, storeRepository, productImageRepository, messageSource, cacheManager, inventoryRepository);
    }

    @Test
    @DisplayName("상품생성")
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

}
