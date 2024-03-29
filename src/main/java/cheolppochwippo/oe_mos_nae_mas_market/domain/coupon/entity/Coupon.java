package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity;

import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String couponInfo;

    private double discount;

    private LocalDateTime created_at;

    private LocalDateTime modified_at;

    private LocalDateTime effective_date;

    private Deleted deleted;

    private long amount;

}
