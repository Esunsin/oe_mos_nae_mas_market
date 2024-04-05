package cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByUser_Id(Long id);

    boolean existsByUserId(Long id);
}
