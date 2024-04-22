package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto;

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
public class IssuedResponse {
    private Long issuedId;
    private Long couponId;
    private Double discount;
    private LocalDateTime effectiveDate;
    private String couponInfo;
    private LocalDateTime createdAt;
    private Deleted deleted;
}
