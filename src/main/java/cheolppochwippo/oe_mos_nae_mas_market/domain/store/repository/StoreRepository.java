package cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByUserId(Long id);

    boolean existsByUserId(Long id);

    List<Store> findAllByIsApprovedFalseOrderByCreatedAt();
    List<Store> findAllByIsApprovedTrueOrderByCreatedAt();
}
