package cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SingleOrderInCartResponse {

    private Long orderId;

    private String productName;

    private Long quantity;

    private Long price;

    private String merchant;

    private Deleted deleted;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public SingleOrderInCartResponse(Order order) {
        this.orderId = order.getId();
        this.productName = order.getProduct().getProductName();
        this.quantity = order.getQuantity();
        this.price = order.getPrice();
        this.merchant = order.getMerchantUid();
        this.deleted = order.getDeleted();
        this.createdAt = order.getCreatedAt();
        this.modifiedAt = order.getModifiedAt();
    }
}

