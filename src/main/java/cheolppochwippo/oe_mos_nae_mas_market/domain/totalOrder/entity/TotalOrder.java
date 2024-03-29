package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalOrder extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;

    private Long discount;

    private Long priceAmount;

    private DeliveryStatus deliveryStatus;

    private Long deliveryCost;

    private Deleted deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
