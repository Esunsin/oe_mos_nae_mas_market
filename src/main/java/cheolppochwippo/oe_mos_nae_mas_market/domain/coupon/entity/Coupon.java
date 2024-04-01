package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponRequest;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "coupons")
public class Coupon extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String couponInfo;

    @Column
    private Double discount;

    @Column
    private LocalDateTime effective_date;

    @Column
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Deleted deleted = Deleted.UNDELETE;

    @Column
    private Long amount;


    public Coupon(CouponRequest couponRequest) {
        this.couponInfo = couponRequest.getCouponInfo();
        this.discount = couponRequest.getDiscount();
        this.effective_date = couponRequest.getEffectiveDate();
        this.amount = couponRequest.getAmount();
    }

    public void update(CouponRequest couponRequest) {
        this.couponInfo = couponRequest.getCouponInfo();
        this.discount = couponRequest.getDiscount();
        this.effective_date = couponRequest.getEffectiveDate();
        this.amount = couponRequest.getAmount();
    }

    public void decreaseAmount() {
        if (this.amount > 0) {
            this.amount--;
        } else {
            throw new IllegalStateException("Coupon amount cannot be negative");
        }
    }

    public void delete() {
        this.deleted = Deleted.DELETE;
    }
}
