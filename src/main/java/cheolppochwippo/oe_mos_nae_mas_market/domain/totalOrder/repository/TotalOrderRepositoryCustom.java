package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderNameDto;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrdersGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TotalOrderRepositoryCustom {

	Optional<TotalOrder> findByUserTotal(Long userId);

	Optional<TotalOrderNameDto> getTotalInfoByUserId(Long userId);

	Optional<TotalOrder> findTotalOrderByUndeleted(User user);

	Optional<Long> pushOrder(TotalOrder totalOrder, Long userId);

	Optional<TotalOrder> findByUserUndeleted(User user);

	void completeOrder(TotalOrder totalOrder);

	Page<TotalOrdersGetResponse> getTotalOrderPageFindByUserId(Long userId, Pageable pageable);
	void decreaseQuantity(Long totalOrderId);


}
