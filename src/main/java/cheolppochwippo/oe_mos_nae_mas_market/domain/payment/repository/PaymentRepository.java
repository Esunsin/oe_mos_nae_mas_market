package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>,PaymentRepositoryCustom{
}
