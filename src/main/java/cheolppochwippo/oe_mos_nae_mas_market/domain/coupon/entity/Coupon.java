package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import java.sql.Time;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Deleted deleted;

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
            this.amount--; // 재고 감소
        } else {
            throw new IllegalStateException("Coupon amount cannot be negative");
        }
    }
}
