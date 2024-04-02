package cheolppochwippo.oe_mos_nae_mas_market.domain.store.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import jakarta.transaction.Transactional;

public interface StoreService {

    StoreResponse createStore(StoreRequest storeRequest, User user);
    StoreResponse updateStore(StoreRequest storeRequest, User user);
}
