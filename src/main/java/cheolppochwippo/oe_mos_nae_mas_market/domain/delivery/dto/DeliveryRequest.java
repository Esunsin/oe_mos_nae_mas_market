package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DeliveryRequest {
    private String address;
}
