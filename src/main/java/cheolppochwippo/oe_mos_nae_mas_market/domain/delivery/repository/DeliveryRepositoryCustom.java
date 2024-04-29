package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.Delivery;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.DeliveryStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;

public interface DeliveryRepositoryCustom {

    List<Delivery> findDeliveryByUser(User user);


}
