package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository.CouponRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.RedisConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.ErrorCode;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableCaching
public class IssuedServiceImpl implements IssuedService {

    private final IssuedRepository issuedRepository;
    private final CouponRepository couponRepository;
    private final RedisConfig redisConfig;
    private final CacheManager cacheManager;

    @Override
    @Transactional
    public IssuedResponse issueCoupon(Long couponId, User user) {
        RLock lock = redisConfig.redissonClient().getLock("couponLock" + couponId);
        try {
            lock.lock();
            List<Issued> issuedCoupons = issuedRepository.findByCouponIdAndUser(couponId, user);

            if (!issuedCoupons.isEmpty()) {
                throw new IllegalStateException("이미 쿠폰을 발급 받으셨습니다.");
            }
            Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException(
                    ErrorCode.COUPON_NOT_FOUND));

            if (coupon.getAmount() > 0) {
                coupon.decreaseAmount();
                couponRepository.save(coupon);

                Issued issuedCoupon = new Issued(coupon, user);
                issuedRepository.save(issuedCoupon);

                Cache issuedCouponCache = cacheManager.getCache("IssuedCoupon");
                if (issuedCouponCache != null) {
                    issuedCouponCache.put(couponId, issuedCoupon);
                } else {
                    throw new IllegalStateException("IssuedCoupon 캐시를 찾을 수 없습니다.");
                }

                return new IssuedResponse(couponId, coupon.getCouponInfo(),
                    issuedCoupon.getCreatedAt(), issuedCoupon.getDeleted());
            } else {
                throw new IllegalArgumentException("남아있는 쿠폰이 없습니다.");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssuedResponse> getIssuedCoupons(User user) {
        return issuedRepository.findCouponByUser(user);
    }
}
