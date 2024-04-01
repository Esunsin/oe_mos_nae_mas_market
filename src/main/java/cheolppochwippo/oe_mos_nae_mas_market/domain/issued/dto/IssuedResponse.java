package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class IssuedResponse {
    private Long couponId;
    private String couponInfo;
    private LocalDateTime createdAt;

    public IssuedResponse(Issued issued) {
        this.couponId = issued.getCoupon().getId();
        this.couponInfo = issued.getCoupon().getCouponInfo();
        this.createdAt = issued.getCreatedAt();
    }
}
