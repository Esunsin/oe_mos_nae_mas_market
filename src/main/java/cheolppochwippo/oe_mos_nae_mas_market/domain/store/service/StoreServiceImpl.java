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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponse createStore(StoreRequest storeRequest, User user) {
        findSeller(user);
        if (RoleEnum.SELLER.equals(user.getRole())) {
            Optional<Store> existingStoreOptional = storeRepository.findByUser_Id(user.getId());
            if (existingStoreOptional.isPresent()) {

                Store store = new Store(storeRequest, user);
                storeRepository.save(store);

                return new StoreResponse(store);

            }
            throw new IllegalArgumentException("상점은 한개만 생성 할 수 있습니다.");

        }
        throw new IllegalArgumentException("판매자만 상점을 생성할 수 있습니다.");

    }


    @Transactional
    public StoreResponse updateStore(StoreRequest storeRequest, User user) {

        findSeller(user);
        if (RoleEnum.SELLER.equals(user.getRole())) {
            Store store = storeRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new NoSuchElementException("상점을 찾을 수 없습니다."));

            store.setStoreName(storeRequest.getStoreName());
            store.setInfo(storeRequest.getInfo());

            return new StoreResponse(store);
        }
        throw new IllegalArgumentException("판매자만 상점을 수정할 수 있습니다.");


    }

    public void findSeller(User user) {
        userRepository.findById(user.getId())
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

    }

}
