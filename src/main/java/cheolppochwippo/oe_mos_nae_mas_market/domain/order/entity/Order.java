package cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    private Long price;

    private boolean cartYN;

    @Column
    @Enumerated(EnumType.STRING)
    private Deleted deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "total_order_id")
    private TotalOrder totalOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatementEnum statement;

    public Order(Long quantity, Product product, User user) {
        this.cartYN = true;
        this.quantity = quantity;
        this.price = product.getPrice();
        this.deleted = Deleted.UNDELETE;
        this.product = product;
        this.user = user;
        this.statement = OrderStatementEnum.WAIT;
    }

    public Order(Long quantity, Product product, User user,boolean cartYN) {
        this.cartYN = cartYN;
        this.quantity = quantity;
        this.price = product.getPrice();
        this.deleted = Deleted.UNDELETE;
        this.product = product;
        this.user = user;
        this.statement = OrderStatementEnum.ORDER;
    }



    public void updateQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
