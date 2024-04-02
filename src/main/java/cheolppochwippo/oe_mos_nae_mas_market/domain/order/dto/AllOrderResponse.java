package cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AllOrderResponse {

    private Long totalPriceByOrder = 0L;

    private Long deliveryCost;

    private Long totalPriceToPay;

    private String content = "4만원이상 무료 배송";

    public AllOrderResponse(List<SingleOrderResponse> singleOrderResponses) {
        for (SingleOrderResponse singleOrderResponse : singleOrderResponses) {
            this.totalPriceByOrder += singleOrderResponse.getPrice() * singleOrderResponse.getQuantity();
        }
        if(totalPriceByOrder > 40000L){
            this.deliveryCost = 0L;
        }
        else {
            this.deliveryCost = 3000L;
        }
        this.totalPriceToPay = totalPriceByOrder + deliveryCost;
    }
}
