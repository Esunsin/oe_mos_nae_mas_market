package cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AllOrderInCartResponse {

    private Long totalPriceByOrder = 0L;

    private Long deliveryCost;

    private Long totalPriceToPay;

    private Integer productSize;

    private String content = "4만원이상 무료 배송";

    public AllOrderInCartResponse(List<SingleOrderInCartResponse> singleOrderResponses) {
        for (SingleOrderInCartResponse singleOrderResponse : singleOrderResponses) {
            this.totalPriceByOrder += singleOrderResponse.getPrice() * singleOrderResponse.getQuantity();
        }
        if(totalPriceByOrder > 40000L){
            this.deliveryCost = 0L;
        }
        else {
            this.deliveryCost = 3000L;
        }
        this.totalPriceToPay = totalPriceByOrder + deliveryCost;
        this.productSize = singleOrderResponses.size();
    }
}
