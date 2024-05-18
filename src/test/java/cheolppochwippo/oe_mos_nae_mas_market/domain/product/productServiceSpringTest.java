package cheolppochwippo.oe_mos_nae_mas_market.domain.product;

import cheolppochwippo.oe_mos_nae_mas_market.domain.image.entity.ProductImage;
import cheolppochwippo.oe_mos_nae_mas_market.domain.image.repository.ProductImageRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductByStoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class productServiceSpringTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Test
    @DisplayName("상품 생성 - jpa saveAll - 1만건")
    void create(){
        UserRequest userReq = new UserRequest("t1", "1234", "00", true);
        User seller = new User(userReq, "1234");
        seller.changeRoleToSeller();
        userRepository.save(seller);

        StoreRequest storeRequest = new StoreRequest("store1", "s");
        Store store = new Store(storeRequest, seller);
        storeRepository.save(store);

        System.out.println("===========================================");
        System.out.println("===========================================");
        System.out.println("===========================================");
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            urls.add("aaa");
        }
        ProductRequest productRequest = new ProductRequest("p", "p", 10000L, 1000L, 50L, urls);
        ProductResponse productResponse = productService.createProduct(productRequest, seller);
        System.out.println("===========================================");
        System.out.println("===========================================");
        System.out.println("===========================================");
        Product product = productRepository.findById(productResponse.getProductId()).get();

        assertEquals(product.getProductName(), productRequest.getProductName());

    }
    @Test
    @DisplayName("상품 생성 - jdbc bulk - 1만건")
    void createBulk() throws SQLException {
        UserRequest userReq = new UserRequest("t1", "1234", "00", true);
        User seller = new User(userReq, "1234");
        seller.changeRoleToSeller();
        userRepository.save(seller);

        StoreRequest storeRequest = new StoreRequest("store1", "s");
        Store store = new Store(storeRequest, seller);
        storeRepository.save(store);

        System.out.println("===========================================");
        System.out.println("===========================================");
        System.out.println("===========================================");
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            urls.add("aaa");
        }
        ProductRequest productRequest = new ProductRequest("p", "p", 10000L, 1000L, 50L, urls);
        ProductResponse productResponse = productService.createProductBulkImage(productRequest, seller);
        System.out.println("===========================================");
        System.out.println("===========================================");
        System.out.println("===========================================");
        List<ProductImage> byProductId = productImageRepository.findByProductId(productResponse.getProductId());

        assertEquals(byProductId.size(), 10000);
    }

    @Test
    public void showProductByStore () throws Exception{
        //give
        UserRequest userReq = new UserRequest("user1", "1234", "00", true);
        User seller = new User(userReq, "1234");
        seller.changeRoleToSeller();
        userRepository.save(seller);

        StoreRequest storeRequest = new StoreRequest("store1", "s");
        Store store = new Store(storeRequest, seller);
        storeRepository.save(store);

        List<String> urls = List.of("https://www.google.com", "https://www.baidu.com");
        List<ProductRequest> productRequests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            productRequests.add(new ProductRequest("p", "p", 10000L, 1000L, 50L, urls));
        }

        List<Product> products = new ArrayList<>();
        for (ProductRequest productRequest : productRequests) {
            products.add(new Product(productRequest, store));
        }
        productRepository.saveAll(products);

        Pageable pageable = PageRequest.of(0,10);

        //when
        System.out.println("===========================================");
        System.out.println("===========================================");
        System.out.println("===========================================");
        List<ProductByStoreResponse> productByStoreResponses = productService.showStoreProduct(pageable, seller);
        System.out.println("===========================================");
        System.out.println("===========================================");
        System.out.println("===========================================");
        //then
        assertEquals(10,productByStoreResponses.size());
    }
}
