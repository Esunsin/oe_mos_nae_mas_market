package cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.repoditory;

import cheolppochwippo.oe_mos_nae_mas_market.domain.inventory.entity.QInventory;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.QProduct;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom{

	private final JpaConfig jpaConfig;

	private final EntityManager entityManager;

	@Transactional
	@Override
	public void updateInventory(){
		jpaConfig.jpaQueryFactory()
			.update(QProduct.product)
			.set(QProduct.product.quantity,QProduct.product.quantity.add(QInventory.inventory.quantity))
			.where(
				QInventory.inventory.deleted.eq(Deleted.UNDELETE)
			).execute();
		jpaConfig.jpaQueryFactory()
			.update(QInventory.inventory)
			.set(QInventory.inventory.deleted,Deleted.DELETE)
			.where(
				QInventory.inventory.deleted.eq(Deleted.UNDELETE)
			).execute();
		entityManager.flush();
		entityManager.clear();
	}

}
