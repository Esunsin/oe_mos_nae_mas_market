package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponses;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepositoryCustom {

	Page<PaymentResponses> getPaymentPageFindByUserId(Long userId, Pageable pageable);

	Optional<Payment> findPaymentKey(String paymentKey);

}
