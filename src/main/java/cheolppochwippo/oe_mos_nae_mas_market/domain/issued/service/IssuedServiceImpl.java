package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository.CouponRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity.Order;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.RedisConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.ErrorCode;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.CouponAlreadyIssuedException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InsufficientQuantityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableCaching
public class IssuedServiceImpl implements IssuedService {

    private final IssuedRepository issuedRepository;
    private final CouponRepository couponRepository;
    private final RedisConfig redisConfig;
    private final MessageSource messageSource;
    private final RedissonClient redissonClient;
    private final ProductRepository productRepository;

    @Override
    public IssuedResponse issueCoupon(Long couponId, User user) {
        RLock lock = redisConfig.redissonClient().getFairLock("couponLock" + couponId);
        try {
            boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
            if (isLocked) {
                try {
                    if (isCouponAlreadyIssued(couponId, user.getId())) {
                        throw new CouponAlreadyIssuedException(
                            messageSource.getMessage("already.issued.coupon", null, Locale.KOREA));
                    }
                    Coupon coupon = getCouponById(couponId);
                    if (coupon.getAmount() > 0) {
                        decreaseCouponAmount(coupon);
                        Issued issuedCoupon = saveIssuedCoupon(coupon, user);
                        cacheIssuedCoupon(couponId, user.getId(), issuedCoupon,
                            coupon.getEffective_date());
                        return createIssuedResponse(couponId, coupon, issuedCoupon);
                    } else {
                        throw new InsufficientQuantityException(
                            messageSource.getMessage("insufficient.quantity.coupon", null,
                                Locale.KOREA));
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                throw new IllegalStateException("락을 획득하지 못했습니다.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락을 획득하는 동안 인터럽트가 발생했습니다.", e);
        }
    }


	private boolean isCouponAlreadyIssued(Long couponId, Long userId) {
        RBucket<Issued> cacheBucket = redisConfig.redissonClient()
            .getBucket("IssuedCoupon:" + couponId + ":" + userId);
        Issued issuedCoupon = cacheBucket.get();
        return issuedCoupon != null;
    }

	private Coupon getCouponById (Long couponId){
		return couponRepository.findById(couponId)
			.orElseThrow(() -> new NoEntityException(
				(messageSource.getMessage("noEntity.coupon", null, Locale.KOREA))));
	}

	private void decreaseCouponAmount (Coupon coupon){
		coupon.decreaseAmount();
		couponRepository.save(coupon);
	}

    @Transactional
    public void decreaseCouponAmountAndProductStock(Long issuedId, Order order) {
        RLock lock = redissonClient.getFairLock(
            "issuedAndProduct:" + issuedId + ":" + order.getProduct().getId());
        try {
            try {
                boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
                if (isLocked) {
                    Issued issuedCoupon = issuedRepository.findById(issuedId)
                        .orElseThrow(() -> new IllegalArgumentException("발급된 쿠폰이 없습니다."));
                    Long couponId = issuedCoupon.getCoupon().getId();
                    Coupon coupon = couponRepository.findById(couponId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰을 찾을 수 없습니다."));
                    coupon.decreaseAmount();
                    couponRepository.save(coupon);

                    Product product = productRepository.findByOrder(order);
                    Long newStock = product.getQuantity() - order.getQuantity();
                    if (newStock < 0) {
                        throw new IllegalArgumentException("재고가 부족합니다.");
                    }
                    product.quatityUpdate(newStock);
                    productRepository.save(product);
                }
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private Issued saveIssuedCoupon(Coupon coupon, User user) {
        Issued issuedCoupon = new Issued(coupon, user);
        return issuedRepository.save(issuedCoupon);
    }

    private void cacheIssuedCoupon(Long couponId, Long userId, Issued issuedCoupon,
        LocalDateTime expirationDate) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime, expirationDate);
        long timeToLive = duration.toMillis();

        RBucket<Issued> cacheBucket = redisConfig.redissonClient()
            .getBucket("IssuedCoupon:" + couponId + ":" + userId);
        cacheBucket.set(issuedCoupon, timeToLive, TimeUnit.MILLISECONDS);
    }

    private IssuedResponse createIssuedResponse(Long couponId, Coupon coupon, Issued issuedCoupon) {
        return new IssuedResponse(issuedCoupon.getId(), couponId, coupon.getCouponInfo(),
            issuedCoupon.getCreatedAt(), issuedCoupon.getDeleted());
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "issuedCoupons", key = "#user.id", cacheManager = "cacheManager")
    public List<IssuedResponse> getIssuedCoupons(User user) {
        return issuedRepository.findCouponByUser(user);
    }
}
