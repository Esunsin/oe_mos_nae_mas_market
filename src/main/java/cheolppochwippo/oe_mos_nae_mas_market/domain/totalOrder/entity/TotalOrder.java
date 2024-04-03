package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import com.querydsl.core.Tuple;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    private String address;

    private Long issueId=0L;

    @Enumerated(EnumType.STRING)
    private Deleted deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private PaymentStatementEnum paymentStatementEnum;

    private String merchantUid;

    public TotalOrder(TotalOrderRequest request,User user, Tuple totalInfo,Tuple totalName,double discount){
        price = totalInfo.get(0, Long.class);
        merchantUid = UUID.randomUUID().toString();
        orderName = totalInfo.get(1, Long.class)>1 ? totalName.get(0, String.class)+" "+
            totalName.get(1, Long.class)+"개 등 "+totalInfo.get(1, Long.class)+"종류의 상품":
            totalName.get(0, String.class)+" "+
                totalName.get(1, Long.class)+"개";
        address = request.getAddress();
        issueId = request.getIssueId();
        paymentStatementEnum = PaymentStatementEnum.WAIT;
        deleted = Deleted.UNDELETE;
        deliveryCost = price>=40000 ? 0L : 3000L;
        this.discount = (long) (price*(1-discount));
        this.priceAmount = (long) (price*(1-discount)-deliveryCost);
        this.user = user;
    }

    public void cancelInProgressOrder(){
        deleted = Deleted.DELETE;
        paymentStatementEnum = PaymentStatementEnum.CANCEL;
    }

    public void completeOrder(){
        deleted = Deleted.DELETE;
        paymentStatementEnum = PaymentStatementEnum.COMPLETE;
    }

}
