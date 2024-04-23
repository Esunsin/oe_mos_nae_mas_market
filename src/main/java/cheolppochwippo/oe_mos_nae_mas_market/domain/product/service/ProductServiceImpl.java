package cheolppochwippo.oe_mos_nae_mas_market.domain.product.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResultResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductShowResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InsufficientQuantityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;
	private final RedissonClient redissonClient;
	private final MessageSource messageSource;
	private final CacheManager cacheManager;

	@Transactional
	@CacheEvict(cacheNames = "products", allEntries = true)
	public ProductResponse createProduct(ProductRequest productRequest, User user) {
		validateSeller(user);
		Store store = storeRepository.findByUser_Id(user.getId())
			.orElseThrow(() -> new NoSuchElementException(
				messageSource.getMessage("noSuch.store", null, Locale.KOREA)));

		Product product = new Product(productRequest, store);
		productRepository.save(product);

		return new ProductResponse(product);
	}
	@Override
	public ProductShowResponse showStoreProduct(Pageable pageable,User user) {
		validateSeller(user);
		List<Product> productList = productRepository.findByStore_User_Id(pageable,user.getId());

		return new ProductShowResponse(
			productList.stream().map(product -> new ProductResultResponse(product)).toList());

	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = "products", allEntries = true)
	public ProductResponse updateProduct(ProductRequest productRequest, Long productId, User user) {
		validateSeller(user);

		Product product = foundProduct(productId);
		product.update(productRequest);

		ProductResultResponse response = new ProductResultResponse(product);
		Objects.requireNonNull(cacheManager.getCache("product")).put(productId,response);

		return new ProductResponse(product);
	}


	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = "product", key = "#productId")
	public ProductResultResponse showProduct(long productId) {
		Product product = foundProduct(productId);

		return new ProductResultResponse(product);
	}


	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = "products", key = "#pageable")
	public ProductShowResponse showAllProduct(Pageable pageable) {
		List<Product> productList = productRepository.findProductsWithQuantityGreaterThanOne(
			pageable);

		return new ProductShowResponse(
			productList.stream().map(product -> new ProductResultResponse(product)).toList());
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = "products", key = "#productId")
	public ProductResponse deleteProduct(Long productId, User user) {
		validateSeller(user);

		Product product = foundProduct(productId);
		product.delete();

		return new ProductResponse(product);
	}

	private Product foundProduct(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new NoSuchElementException(
				messageSource.getMessage("noEntity.product", null, Locale.KOREA)));
	}

	private void validateSeller(User user) {
		if (!RoleEnum.SELLER.equals(user.getRole())) {
			throw new NoPermissionException(
				messageSource.getMessage("noPermission.role.seller", null, Locale.KOREA));
		}
	}


	//재고 감소시켜주는 메소드
	public void decreaseProductStock(Order order) {
		RLock lock = redissonClient.getFairLock("product" + order.getProduct().getId());
		try {
			try {
				boolean isLocked = lock.tryLock(1000, 3000, TimeUnit.SECONDS);
				if (isLocked) {
					decreaseProductStockTransaction(order);
				}
			} finally {
				lock.unlock();
			}
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			System.out.println(e.getMessage());
		}

	}

	@Transactional
	public void decreaseProductStockTransaction(Order order) {
		Product product = productRepository.findByOrder(order);
		Long newStock = product.getQuantity() - order.getQuantity();
		if (newStock < 0) {
			throw new InsufficientQuantityException(
				messageSource.getMessage("insufficient.quantity.product", null,
					Locale.KOREA));
		}
		product.quatityUpdate(newStock);
		productRepository.save(product);
	}


	@Transactional(readOnly = true)
	public ProductShowResponse showAllProductWithValue(Pageable pageable, String searchValue) {
		List<Product> productList;
		if (searchValue == null) {
			log.info("없을때");
			productList = productRepository.findProductsWithQuantityGreaterThanOne(pageable);
		} else {
			log.info("있을때");
			productList = productRepository.findProductsWithQuantityGreaterThanOneAndSearchValue(
				pageable, searchValue);
		}
		return new ProductShowResponse(
			productList.stream().map(ProductResultResponse::new).toList());
	}
}