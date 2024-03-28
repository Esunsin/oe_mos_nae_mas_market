package cheolppochwippo.oe_mos_nae_mas_market.domain.refreshToken.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.refreshToken.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

	Optional<RefreshToken> findByUserId(Long userId);

}
