package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.DeliveryStatus;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TotalOrderResponse {

    private Long totalOrderId;

    private Long price;

    private Long discount;

    private Long priceAmount;

    private Long deliveryCost;

    private DeliveryStatus deliveryStatus;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public TotalOrderResponse(TotalOrder totalOrder) {
        this.totalOrderId = totalOrder.getId();
        this.price = totalOrder.getPrice();
        this.discount = totalOrder.getDiscount();
        this.priceAmount = totalOrder.getPriceAmount();
        this.deliveryCost = totalOrder.getDeliveryCost();
//
        this.createdAt = totalOrder.getCreatedAt();
        this.modifiedAt = totalOrder.getModifiedAt();
    }
}