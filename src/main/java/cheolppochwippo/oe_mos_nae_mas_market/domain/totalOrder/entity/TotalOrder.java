package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalOrder extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;

    private Long discount;

    private Long priceAmount;

    private Long deliveryCost;

    private String orderName;


    @Enumerated(EnumType.STRING)
    private Deleted deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private PaymentStatementEnum paymentStatementEnum;

    private String merchantUid;

    public TotalOrder(TotalOrderRequest request,User user,double discount){
        price = request.getAmount();
        orderName = request.getOrderName();
        merchantUid = UUID.randomUUID().toString();
        paymentStatementEnum = PaymentStatementEnum.WAIT;
        deleted = Deleted.UNDELETE;
        deliveryCost = price>=40000 ? 0L : 3000L;
        this.discount = (long) (request.getAmount()*(1-discount));
        this.priceAmount = (long) (request.getAmount()*(1-discount)-deliveryCost);
        this.user = user;
    }

}
