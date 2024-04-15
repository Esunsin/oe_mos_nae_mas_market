package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderNameDto;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrdersGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import com.querydsl.core.Tuple;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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

	private final MessageSource messageSource;

	@Transactional
	@Override
	public TotalOrderResponse createTotalOrder(User user, TotalOrderRequest request) {
		Optional<TotalOrder> total = totalOrderRepository.findByUserUndeleted(user);
		if (total.isPresent()) {
			total.get().cancelInProgressOrder();
			totalOrderRepository.save(total.get());
		}
		TotalOrderNameDto totalInfo = totalOrderRepository.getTotalInfoByUserId(user.getId()).orElseThrow(
			() -> new NoEntityException(
				messageSource.getMessage("noEntity.totalOrder", null, Locale.KOREA))
		);
		double discount = request.getIssuedId() == 0 ? 0
			: issuedRepository.getDiscountFindById(user.getId(), request.getIssuedId()).orElseThrow(
				() -> new NoPermissionException(messageSource.getMessage("noPermission.coupon", null, Locale.KOREA))
			);
		TotalOrder totalOrder = new TotalOrder(request, user, totalInfo, discount);
		totalOrderRepository.save(totalOrder);
		totalOrderRepository.pushOrder(totalOrder, user.getId());
		TotalOrderResponse response = new TotalOrderResponse(totalOrder, user);
		return response;
	}

	@Override
	public TotalOrderGetResponse getTotalOrder(User user, Long totalOrderId) {
		TotalOrder totalOrder = totalOrderRepository.findById(totalOrderId).orElseThrow(
			() -> new NoEntityException(
				messageSource.getMessage("noEntity.totalOrder", null, Locale.KOREA))
		);
		if (!Objects.equals(totalOrder.getUser().getId(), user.getId())) {
			throw new NoPermissionException(messageSource.getMessage("noPermission.totalOrder", null, Locale.KOREA));
		}
		return new TotalOrderGetResponse(totalOrder);
	}

	@Override
	public Page<TotalOrdersGetResponse> getTotalOrders(User user, int page) {
		Pageable pageable = PageRequest.of(page-1, 10);
		return totalOrderRepository.getTotalOrderPageFindByUserId(user.getId(), pageable);
	}

}
