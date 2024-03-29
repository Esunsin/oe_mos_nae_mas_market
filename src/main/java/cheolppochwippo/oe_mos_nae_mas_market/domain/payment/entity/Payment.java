package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;

    private Long totalPrice;

    private String merchantUid;

    private Long approvalNumber;

    @Enumerated(EnumType.STRING)
    private PaymentStatementEnum statement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "total_order_id", nullable = false)
    private TotalOrder totalOrder;
}