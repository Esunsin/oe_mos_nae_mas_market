package cheolppochwippo.oe_mos_nae_mas_market.domain.store.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponse createStore(StoreRequest storeRequest, User user) {
            User seller = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (RoleEnum.SELLER.equals(seller.getRole())) {

            Store store = new Store(storeRequest, seller);
            storeRepository.save(store);

            return new StoreResponse(store);
        } else {
            throw new IllegalArgumentException("판매자만 상점을 생성할 수 있습니다.");
        }
    }


    @Transactional
    public StoreResponse updateStore(StoreRequest storeRequest, User user) {
        // User seller = new User(user.getId(), user.getUsername(), user.getRole());

        Store store = storeRepository.findByUser_Id(user.getId());
        store.setStoreName(storeRequest.getStoreName());
        store.setInfo(storeRequest.getInfo());

        return new StoreResponse(store);

    }

}
