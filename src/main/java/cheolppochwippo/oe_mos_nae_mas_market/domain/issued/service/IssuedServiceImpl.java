package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository.CouponRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.RedisConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.CouponAlreadyIssuedException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InsufficientQuantityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cheolppochwippo.oe_mos_nae_mas_market.global.redissonLockAoop.RedissonLockAnnotation;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
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
    private final MessageSource messageSource;
    private final RedissonClient redissonClient;

    @Override
    public IssuedResponse issueCoupon(Long couponId, User user) {
        List<Issued> issuedCoupons = issuedRepository.findByCouponIdAndUser(couponId, user);
        if (!issuedCoupons.isEmpty()) {
            throw new CouponAlreadyIssuedException(
                (messageSource.getMessage("already.issued.coupon", null, Locale.KOREA)));
        }
        Coupon coupon = getCouponById(couponId);
        Issued issuedCoupon = saveIssuedCoupon(coupon, user);
        Coupon findCoupon = getIssuedCoupon(coupon.getId());
        decreaseCouponAmount(findCoupon);
        return createIssuedResponse(couponId, coupon, issuedCoupon);
    }

    public Coupon getCouponById(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new NoEntityException(
                messageSource.getMessage("coupon.notFound", null, Locale.KOREA)));

        if (coupon.getAmount() <= 0) {
            throw new InsufficientQuantityException(
                messageSource.getMessage("insufficient.quantity.coupon",
                    null, Locale.KOREA));
        }
        return coupon;
    }

    @RedissonLockAnnotation
    public void decreaseCouponAmount(Coupon coupon) {
        if (coupon.getAmount() > 0) {
            coupon.decreaseAmount();
            couponRepository.save(coupon);
        } else {
            throw new InsufficientQuantityException(
                messageSource.getMessage("insufficient.quantity.coupon",
                    null, Locale.KOREA));
        }
    }

    private Coupon getIssuedCoupon(Long issuedId) {
        Issued issuedCoupon = issuedRepository.findById(issuedId)
            .orElseThrow(() -> new NoEntityException(
                (messageSource.getMessage("noEntity.coupon", null, Locale.KOREA))));
        Coupon coupon = issuedCoupon.getCoupon();
        return coupon;
    }


    private Issued saveIssuedCoupon(Coupon coupon, User user) {
        Issued issuedCoupon = new Issued(coupon, user);
        return issuedRepository.save(issuedCoupon);
    }


    private IssuedResponse createIssuedResponse(Long couponId, Coupon coupon, Issued issuedCoupon) {
        return new IssuedResponse(issuedCoupon.getId(), couponId,
            issuedCoupon.getCoupon().getDiscount(), issuedCoupon.getCoupon().getEffective_date(),
            coupon.getCouponInfo(),
            issuedCoupon.getCreatedAt(), issuedCoupon.getDeleted());
    }


    @Override
    @Transactional(readOnly = true)
    public List<IssuedResponse> getIssuedCoupons(User user) {
        return issuedRepository.findCouponByUser(user);
    }
}