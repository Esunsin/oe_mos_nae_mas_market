package cheolppochwippo.oe_mos_nae_mas_market.domain.paymentCancel.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCancel extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cancelMerchantUid;

    private Long refundAmount;

    private String content;

    private Deleted deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;
}
