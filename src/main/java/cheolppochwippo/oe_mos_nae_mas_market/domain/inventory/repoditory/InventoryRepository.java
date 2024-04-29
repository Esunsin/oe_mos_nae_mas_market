package cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.repoditory;

import cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
