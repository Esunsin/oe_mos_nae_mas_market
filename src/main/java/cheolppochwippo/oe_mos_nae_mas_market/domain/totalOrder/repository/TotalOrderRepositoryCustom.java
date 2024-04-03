package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrdersGetResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import com.querydsl.core.Tuple;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TotalOrderRepositoryCustom {

	Optional<TotalOrder> findByUserTotal(Long userId);

	Optional<Tuple> getTotalInfoByUserId(Long userId);

	Optional<TotalOrder> findTotalOrderByUndeleted(User user);

	Optional<Long> pushOrder(TotalOrder totalOrder, Long userId);

	Optional<TotalOrder> findByUserUndeleted(User user);

	Optional<Tuple> getTotalNameUserId(Long userId);

	void completeOrder(TotalOrder totalOrder);

	Page<TotalOrdersGetResponse> getTotalOrderPageFindByUserId(Long userId, Pageable pageable);

}
