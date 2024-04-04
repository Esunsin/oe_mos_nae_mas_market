package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private Deleted deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Delivery(User user, DeliveryRequest deliveryRequest) {
        this.user = user;
        this.address = deliveryRequest.getAddress();
    }

    public void update(DeliveryRequest deliveryRequest) {
        this.address = deliveryRequest.getAddress();
    }

    public void delete() {
        this.deleted = Deleted.DELETE;
    }

    public Delivery(TotalOrder totalOrder){
        address = totalOrder.getAddress();
        deleted = Deleted.UNDELETE;
        user = totalOrder.getUser();
    }
}
