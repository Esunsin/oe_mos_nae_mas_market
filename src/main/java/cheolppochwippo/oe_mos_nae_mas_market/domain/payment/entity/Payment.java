package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentCancelRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;

    @Column(nullable = false, name = "pay_amount")
    private Long amount;
    @Column(nullable = false, name = "pay_name")
    private String orderName;
    @Column(nullable = false, name = "order_id")
    private String orderId;

    private boolean paySuccessYN;

    @Column
    private String paymentKey;

    @Column
    private boolean cancelYN;
    @Column
    private String cancelReason;

    @Enumerated(EnumType.STRING)
    private PaymentStatementEnum statement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "total_order_id", nullable = false)
    private TotalOrder totalOrder;

    public Payment(PaymentRequest request,TotalOrder totalOrder){
        amount= request.getAmount();
        orderName = totalOrder.getOrderName();
        orderId = request.getOrderId();
        paymentKey = request.getPaymentKey();
        paySuccessYN = true;
        cancelYN = false;
        statement = PaymentStatementEnum.COMPLETE;
        this.totalOrder = totalOrder;
    }

    public Payment(Payment payment, PaymentCancelRequest paymentCancelRequest){
        amount = payment.getAmount();
        orderName = payment.getOrderName();
        orderId = payment.getOrderId();
        paymentKey = payment.getPaymentKey();
        paySuccessYN = true;
        cancelYN = true;
        statement = PaymentStatementEnum.CANCEL;
        totalOrder = payment.getTotalOrder();
        cancelReason = paymentCancelRequest.getCancelReason();
    }
}
