package cheolppochwippo.oe_mos_nae_mas_market.domain.store.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.CreationLimitExceededException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Transactional
    public StoreResponse createStore(StoreRequest storeRequest, User user) {
        User seller = getSeller(user);
        checkExistingStore(seller);
        Store store = new Store(storeRequest, seller);
        store.approve();
        storeRepository.save(store);
        return new StoreResponse(store);
    }

    @Transactional
    public StoreResponse updateStore(StoreRequest storeRequest, User user) {
        User seller = getSeller(user);
        Store store = getStoreByUser(seller);
        store.update(storeRequest);
        return new StoreResponse(store);
    }

    @Override
    public StoreResponse showStore(User user) {
        User seller = getSeller(user);
        Store store = getStoreByUser(seller);
        return new StoreResponse(store);
    }

    @Transactional
    @Override
    public StoreResponse approveStore(Long requestId, User user) {
        validateAdmin(user);
        Store store = storeRepository.findById(requestId).orElseThrow(
                () -> new NoSuchElementException(
                        messageSource.getMessage("noSuch.store", null, Locale.KOREA))
        );
        store.approve();
        User seller = store.getUser();
        seller.changeRoleToSeller();
        return new StoreResponse(store);
    }

    @Override
    public List<StoreResponse> showFalseStore(User user) {
        validateAdmin(user);
        List<Store> falseStoreList = storeRepository.findAllByIsApprovedFalseOrderByCreatedAt();

        return falseStoreList.stream()
            .map(StoreResponse::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<StoreResponse> showTrueStore(User user) {
        validateAdmin(user);
        List<Store> trueStoreList = storeRepository.findAllByIsApprovedTrueOrderByCreatedAt();

        return trueStoreList.stream()
            .map(StoreResponse::new)
            .collect(Collectors.toList());
    }
    private Store getStoreByUser(User seller) {
        Store store = storeRepository.findByUserId(seller.getId()).orElseThrow(
                () -> new NoSuchElementException(
                        messageSource.getMessage("noSuch.store", null, Locale.KOREA))
        );
        return store;
    }

    private void validateAdmin(User user) {
        if (!RoleEnum.ADMIN.equals(user.getRole())) {
            throw new NoPermissionException(
                messageSource.getMessage("noPermission.role.admin", null, Locale.KOREA));
        }
    }

    private User getSeller(User user) {
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NoEntityException(
                        messageSource.getMessage("noEntity.user", null, Locale.KOREA)));
        if(findUser.getRole() != RoleEnum.SELLER){
            throw new NoPermissionException(
                    messageSource.getMessage("noPermission.role.seller", null, Locale.KOREA)
            );
        }
        return findUser;
    }

    private void checkExistingStore(User seller) {
        if (storeRepository.existsByUserId(seller.getId())) {
            throw new CreationLimitExceededException(
                messageSource.getMessage("create.limit.shop", null, Locale.KOREA));
        }
    }
}
