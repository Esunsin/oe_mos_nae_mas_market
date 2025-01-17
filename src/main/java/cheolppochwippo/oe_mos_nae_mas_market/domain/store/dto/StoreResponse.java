package cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {

    private String storeName;

    private String info;
    private boolean isApproved;

    public StoreResponse(Store store) {
        this.storeName = store.getStoreName();
        this.info = store.getInfo();
        this.isApproved = store.isApproved();

    }

}
