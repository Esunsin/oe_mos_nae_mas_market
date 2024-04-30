package cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.QuantityUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private Deleted deleted;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Inventory(QuantityUpdateRequest productRequest, Product product) {
        this.quantity = productRequest.getQuantity();
        this.product = product;
        this.deleted = Deleted.UNDELETE;
    }
}