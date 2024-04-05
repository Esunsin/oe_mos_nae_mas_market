package cheolppochwippo.oe_mos_nae_mas_market.domain.order.store;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum.CONSUMER;
import static cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum.SELLER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.service.StoreServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    private StoreServiceImpl storeService;

    private User seller;
    private User customer;
    private Store store;
    private StoreRequest storeRequest;

    @BeforeEach
    void setUp() {

        storeService = new StoreServiceImpl(storeRepository, userRepository);

        seller = new User("seller", "password", SELLER);
        customer = new User("customer", "password", CONSUMER);
        store = new Store(1L, "Test Store", "Test Store Info", Deleted.UNDELETE, seller);

        storeRequest = new StoreRequest("Test Store", "Test Store Info");
    }

    @Test
    @DisplayName("상점 생성_성공")
    void createStore_Success() {
        // Given
        when(userRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(storeRepository.existsByUserId(seller.getId())).thenReturn(false);
        when(storeRepository.save(any())).thenReturn(new Store(storeRequest, seller));

        // When
        StoreResponse result = storeService.createStore(
            new StoreRequest("Test Store", "Test Store Info"), seller);

        // Then
        assertEquals("Test Store", result.getStoreName());
        assertEquals("Test Store Info", result.getInfo());
    }

    @Test
    @DisplayName("상점 생성_유저가_판매자가_아닐_경우")
    void createStore_UserNotSeller_ThrowsIllegalArgumentException() {
        // Given
        when(userRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        // When, Then
        assertThrows(IllegalArgumentException.class,
            () -> storeService.createStore(new StoreRequest("Test Store", "Test Store Info"),
                customer));
    }

    @Test
    @DisplayName("상점 생성_존재할때_오류")
    void createStore_ExistingStoreFound_ThrowsIllegalArgumentException() {
        // Given
        when(userRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(storeRepository.existsByUserId(seller.getId())).thenReturn(true);

        // When, Then
        assertThrows(IllegalArgumentException.class,
            () -> storeService.createStore(new StoreRequest("Test Store", "Test Store Info"),
                seller));
    }

    @Test
    @DisplayName("상점 수정_성공")
    void updateStore_Success() {
        // Given
        when(userRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(storeRepository.findByUser_Id(seller.getId())).thenReturn(Optional.of(store));

        // When
        StoreResponse result = storeService.updateStore(
            new StoreRequest("Test updateStore", "Test updateStore Info"), seller);

        // Then
        assertEquals("Test updateStore", result.getStoreName());
        assertEquals("Test updateStore Info", result.getInfo());
    }

    @Test
    @DisplayName("상점 수정_상점_없음_오류")
    void updateStore_StoreNotFound_ThrowsNoSuchElementException() {
        // Given
        when(userRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(storeRepository.findByUser_Id(seller.getId())).thenReturn(Optional.empty());

        // When, Then
        assertThrows(NoSuchElementException.class,
            () -> storeService.updateStore(new StoreRequest("Updated Store", "Updated Store Info"),
                seller));
    }

    @Test
    @DisplayName("상점 수정_유저가_판매자가_아닐_경우")
    void updateStore_UserNotSeller_ThrowsIllegalArgumentException() {
        // Given
        when(userRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        // When, Then
        assertThrows(IllegalArgumentException.class,
            () -> storeService.updateStore(new StoreRequest("Updated Store", "Updated Store Info"),
                customer));
    }
}

