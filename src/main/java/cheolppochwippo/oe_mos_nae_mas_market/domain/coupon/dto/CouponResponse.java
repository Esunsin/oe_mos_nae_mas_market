package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
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
    private Long couponId;
    private String couponInfo;
    private Double discount;
    private LocalDateTime effectiveDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Deleted deleted;

    public CouponResponse(Coupon coupon) {
        this.couponId = coupon.getId();
        this.couponInfo = coupon.getCouponInfo();
        this.discount = coupon.getDiscount();
        this.effectiveDate = coupon.getEffective_date();
        this.createdAt = coupon.getCreatedAt();
        this.modifiedAt = coupon.getModifiedAt();
        this.deleted = coupon.getDeleted();
    }
}
