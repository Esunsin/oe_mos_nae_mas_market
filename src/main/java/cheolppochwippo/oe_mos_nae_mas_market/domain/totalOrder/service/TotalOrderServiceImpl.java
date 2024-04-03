package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import com.querydsl.core.Tuple;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TotalOrderServiceImpl implements TotalOrderService{

	private final TotalOrderRepository totalOrderRepository;

	private final IssuedRepository issuedRepository;

	@Transactional
	public TotalOrderResponse createTotalOrder(User user, TotalOrderRequest request){
		Optional<TotalOrder> total = totalOrderRepository.findByUserUndeleted(user);
		if(total.isPresent()){
			total.get().cancelInProgressOrder();
			totalOrderRepository.save(total.get());
		}
		Tuple totalInfo = totalOrderRepository.getTotalInfoByUserId(user.getId()).orElseThrow(
			()-> new IllegalArgumentException("현재 진행중인 주문이 없습니다")
		);
		Tuple totalName = totalOrderRepository.getTotalNameUserId(user.getId()).orElseThrow(
			()-> new IllegalArgumentException("현재 진행중인 주문이 없습니다")
		);

		double discount = issuedRepository.getDiscountFindById(user.getId(),request.getIssueId()).orElseThrow(
			()-> new IllegalArgumentException("유효하지 않은 쿠폰 입니다.")
		);
		TotalOrder totalOrder = new TotalOrder(request,user,totalInfo,totalName,discount);
		totalOrderRepository.save(totalOrder);
		totalOrderRepository.pushOrder(totalOrder,user.getId());
		TotalOrderResponse response  = new TotalOrderResponse(totalOrder);
		return response ;
	}

}
