package cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto;

import lombok.Getter;

@Getter
public class SingOrderResponse {

    private String productName;
    private Long quantity;
    private Long price;

    public SingOrderResponse(String productName, Long quantity, Long price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}
