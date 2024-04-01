package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedRepository extends JpaRepository<Issued,Long>{

    List<Issued> findByCouponIdAndUser(Long couponId, User user);

    List<Issued> findByUser(User user);
}
