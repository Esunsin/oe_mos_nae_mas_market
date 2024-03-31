package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CouponResponse {
    private String couponInfo;
    private Double discount;
    private Long amount;
    private LocalDateTime effectiveDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CouponResponse(Coupon coupon){
        this.couponInfo = coupon.getCouponInfo();
        this.discount = coupon.getDiscount();
        this.amount = coupon.getAmount();
        this.effectiveDate = coupon.getEffective_date();
        this.createdAt = coupon.getCreatedAt();
        this.modifiedAt = coupon.getModifiedAt();
    }
}
