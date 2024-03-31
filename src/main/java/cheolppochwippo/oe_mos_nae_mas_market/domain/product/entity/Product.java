package cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String info;

    private Long real_price;

    private Long price;

    private Long discount;

    private Long quantity;

    private Deleted deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;
}
