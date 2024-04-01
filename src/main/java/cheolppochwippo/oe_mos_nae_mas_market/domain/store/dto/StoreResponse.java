package cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class StoreResponse {
    private String storeName;

    private String info;
    public StoreResponse(Store store){
        this.storeName = store.getStoreName();
        this.info = store.getInfo();
    }
}
