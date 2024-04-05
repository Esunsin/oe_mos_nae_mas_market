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
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

	@Transactional
	@CacheEvict(cacheNames = "products", allEntries = true)
	public ProductResponse createProduct(ProductRequest productRequest, User user) {
		validateSeller(user);
		Store store = storeRepository.findByUser_Id(user.getId())
			.orElseThrow(() -> new NoSuchElementException("상점을 찾을 수 없습니다."));

		Product product = new Product(productRequest, store);
		productRepository.save(product);

		return new ProductResponse(product);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = "products", allEntries = true)
	public ProductResponse updateProduct(ProductRequest productRequest, Long productId, User user) {
		validateSeller(user);

		Product product = foundProduct(productId);
		product.update(productRequest);

		return new ProductResponse(product);
	}


	@Override
	@Transactional(readOnly = true)
	@CacheEvict(cacheNames = "products", allEntries = true)
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
			.orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다."));
	}

	private void validateSeller(User user) {
		if (!RoleEnum.SELLER.equals(user.getRole())) {
			throw new IllegalArgumentException("판매자만 상품을 등록할 수 있습니다.");
		}
	}


	//재고 다시 증가시켜주는 메서드
	public void updateQuantity(Order order) {
		RLock lock = redissonClient.getFairLock("product" + order.getProduct().getId());
		try {
			lock.lock(); // 잠금 획득
			Product product = productRepository.findByOrder(order);
			product.quatityUpdate(product.getQuantity() + order.getQuantity());
			productRepository.save(product);
		} finally {
			lock.unlock(); // 잠금 해제
		}
	}

	//재고 감소시켜주는 메소드

	public void decreaseProductStock(Order order) {
		RLock lock = redissonClient.getFairLock("product" + order.getProduct().getId());
		try {
			lock.lock();

			Product product = productRepository.findByOrder(order);
			Long newStock = product.getQuantity() - order.getQuantity();
			product.quatityUpdate(newStock);
			productRepository.save(product);
			if (newStock < 0) {
				throw new IllegalArgumentException("재고가 부족합니다.");
			}
		} finally {
			lock.unlock();
		}

	}

}
