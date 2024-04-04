package cheolppochwippo.oe_mos_nae_mas_market.domain.store.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    @Transactional
    public StoreResponse createStore(StoreRequest storeRequest, User user) {
        User seller = getUser(user);
        checkUserRole(user);
        checkExistingStore(seller);

        Store store = new Store(storeRequest, seller);
        storeRepository.save(store);

        return new StoreResponse(store);

    }

    @Transactional
    public StoreResponse updateStore(StoreRequest storeRequest, User user) {

        User seller = getUser(user);
        checkUserRole(user);
        Store store = storeRepository.findByUser_Id(seller.getId())
            .orElseThrow(() -> new NoSuchElementException("상점을 찾을 수 없습니다."));

        store.update(storeRequest);

        return new StoreResponse(store);

    }

    private User getUser(User user) {
        return userRepository.findById(user.getId())
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    private void checkUserRole(User user) {
        if (!RoleEnum.SELLER.equals(user.getRole())) {
            throw new IllegalArgumentException("판매자만 상점을 생성 또는 수정할 수 있습니다.");
        }
    }

    private void checkExistingStore(User seller) {
        if (storeRepository.existsByUserId(seller.getId())) {
            throw new IllegalArgumentException("상점은 한개만 생성 할 수 있습니다.");
        }
    }


}
