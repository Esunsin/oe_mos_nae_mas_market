package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotalOrderServiceImpl implements TotalOrderService{

	private final TotalOrderRepository totalOrderRepository;

	private final IssuedRepository issuedRepository;

	public TotalOrderResponse createTotalOrder(User user, TotalOrderRequest request){
		Long totalPrice = totalOrderRepository.getTotalPriceByUserId(user.getId());
		if(!Objects.equals(totalPrice, request.getAmount())){
			throw new IllegalArgumentException("입력된 가격이 상품의 가격과 맞지 않습니다.");
		}
		double discount = 0.3;
		// issuedRepository에서 가져오는 작업 필요
		TotalOrder totalOrder = new TotalOrder(request,user,discount);
		totalOrderRepository.save(totalOrder);
		TotalOrderResponse response  = new TotalOrderResponse(totalOrder);
		return response ;
	}
}
