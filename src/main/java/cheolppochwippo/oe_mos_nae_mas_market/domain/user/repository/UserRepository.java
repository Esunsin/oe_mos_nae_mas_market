package cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>,UserRepositoryCustom {

    Optional<User> findByUsername(String username);
}

