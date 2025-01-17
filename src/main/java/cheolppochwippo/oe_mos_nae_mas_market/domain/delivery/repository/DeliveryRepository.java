package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.Delivery;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.DeliveryStatementEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery,Long>,DeliveryRepositoryCustom {

    List<Delivery> findByDeliveryStatementEnum(DeliveryStatementEnum deliveryStatementEnum);
}
