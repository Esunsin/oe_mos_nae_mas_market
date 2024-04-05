package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrdersGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import com.querydsl.core.Tuple;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TotalOrderServiceImpl implements TotalOrderService {

	private final TotalOrderRepository totalOrderRepository;

	private final IssuedRepository issuedRepository;

	@Transactional
	@Override
	public TotalOrderResponse createTotalOrder(User user, TotalOrderRequest request) {
		Optional<TotalOrder> total = totalOrderRepository.findByUserUndeleted(user);
		if (total.isPresent()) {
			total.get().cancelInProgressOrder();
			totalOrderRepository.save(total.get());
		}
		Tuple totalInfo = totalOrderRepository.getTotalInfoByUserId(user.getId()).orElseThrow(
			() -> new NoEntityException("현재 진행중인 주문이 없습니다")
		);
		Tuple totalName = totalOrderRepository.getTotalNameUserId(user.getId()).orElseThrow(
			() -> new NoEntityException("현재 진행중인 주문이 없습니다")
		);

		double discount = request.getIssuedId() == 0 ? 0
			: issuedRepository.getDiscountFindById(user.getId(), request.getIssuedId()).orElseThrow(
				() -> new NoPermissionException("유효하지 않은 쿠폰 입니다.")
			);
		TotalOrder totalOrder = new TotalOrder(request, user, totalInfo, totalName, discount);
		totalOrderRepository.save(totalOrder);
		totalOrderRepository.pushOrder(totalOrder, user.getId());
		TotalOrderResponse response = new TotalOrderResponse(totalOrder);
		return response;
	}

	@Override
	public TotalOrderGetResponse getTotalOrder(User user, Long totalOrderId) {
		TotalOrder totalOrder = totalOrderRepository.findById(totalOrderId).orElseThrow(
			() -> new NoEntityException("존재하지 않는 주문정보 입니다.")
		);
		if (!Objects.equals(totalOrder.getUser().getId(), user.getId())) {
			throw new NoPermissionException("조회하실 권한이 없습니다.");
		}
		return new TotalOrderGetResponse(totalOrder);
	}

	@Override
	public Page<TotalOrdersGetResponse> getTotalOrders(User user, int page) {
		Pageable pageable = PageRequest.of(page-1, 10);
		return totalOrderRepository.getTotalOrderPageFindByUserId(user.getId(), pageable);
	}

}
