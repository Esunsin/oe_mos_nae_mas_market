package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.Delivery;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;

public interface DeliveryService {

    DeliveryResponse createDelivery(DeliveryRequest deliveryRequest, User user);

    DeliveryResponse updateDelivery(Long deliveryId, DeliveryRequest deliveryRequest, User user);

    void deleteDelivery(Long deliveryId, User user);

    List<DeliveryResponse> getDeliveries(User user);
}
