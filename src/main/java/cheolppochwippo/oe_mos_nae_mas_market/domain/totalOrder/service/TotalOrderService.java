package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrdersGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import org.springframework.data.domain.Page;

public interface TotalOrderService {

	TotalOrderResponse createTotalOrder(User user, TotalOrderRequest request);

	TotalOrderGetResponse getTotalOrder(User user,Long totalOrderId);

	Page<TotalOrdersGetResponse> getTotalOrders(User user,int page);
}
