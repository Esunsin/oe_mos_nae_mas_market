package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long>, CouponRepositoryCustom {
}
