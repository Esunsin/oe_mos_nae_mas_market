package cheolppochwippo.oe_mos_nae_mas_market.domain.Issued;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository.CouponRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.service.CouponService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service.IssuedService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service.IssuedServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.RedisConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
public class IssuedServiceTest {

    @Mock
    IssuedRepository issuedRepository;

    @Mock
    CouponRepository couponRepository;

    @Mock
    RedisConfig redisConfig;

    @Mock
    private RLock lock;
    @Mock
    private RedissonClient redissonClient;

    @Mock
    CacheManager cacheManager;
    @Mock
    CouponService couponService;

    IssuedService issuedService;

    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void before() {
        issuedService = new IssuedServiceImpl(issuedRepository, couponRepository, redisConfig,
            cacheManager);
    }

    @BeforeEach
    void setUp() {
        RedissonClient redissonClientMock = mock(RedissonClient.class);
        when(redisConfig.redissonClient()).thenReturn(redissonClientMock);
    }

    @Test
    @DisplayName("쿠폰 발급 성공")
    void issueCoupon_Success() {
        Long couponId = 100L;
        User user = new User(100L, "customer", "CONSUMER");

        Coupon coupon = new Coupon(couponId, "Test Coupon", 0.1D, LocalDateTime.now(),
            Deleted.UNDELETE, 100L);
        Issued issuedCoupon = new Issued(coupon, user);
        given(couponRepository.findById(100L)).willReturn(Optional.of(coupon));

        CacheManager cacheManagerMock = mock(CacheManager.class);
        Cache cacheMock = mock(Cache.class);
        given(cacheManagerMock.getCache(any())).willReturn(cacheMock);

        RedisConfig redisConfigMock = Mockito.mock(RedisConfig.class);
        RedissonClient redissonClientMock = Mockito.mock(RedissonClient.class);
        RLock rLockMock = Mockito.mock(RLock.class);

        Mockito.when(redisConfigMock.redissonClient()).thenReturn(redissonClientMock);
        Mockito.when(redissonClientMock.getLock(anyString())).thenReturn(rLockMock);

        IssuedServiceImpl issuedService = new IssuedServiceImpl(issuedRepository, couponRepository,
            redisConfigMock, cacheManagerMock);

        // when
        IssuedResponse issuedResponse = issuedService.issueCoupon(couponId, user);

        // then
        assertNotNull(issuedResponse);
        assertEquals(couponId, issuedResponse.getCouponId());
        assertEquals(coupon.getCouponInfo(), issuedResponse.getCouponInfo());
        verify(cacheMock, times(1)).put(eq(couponId), any(Issued.class));
    }

    @Test
    @DisplayName("이미 발급된 쿠폰 존재")
    void issueCoupon_AlreadyIssued() {
        // given
        Long couponId = 100L;
        User user = new User(100L, "customer", "CONSUMER");

        List<Issued> issuedCoupons = new ArrayList<>();
        issuedCoupons.add(new Issued(
            new Coupon(couponId, "Test Coupon", 0.1D, LocalDateTime.now(), Deleted.UNDELETE, 100L),
            user));

        RedisConfig redisConfigMock = Mockito.mock(RedisConfig.class);
        RedissonClient redissonClientMock = Mockito.mock(RedissonClient.class);
        RLock rLockMock = Mockito.mock(RLock.class);

        Mockito.when(redisConfigMock.redissonClient()).thenReturn(redissonClientMock);
        Mockito.when(redissonClientMock.getLock(anyString())).thenReturn(rLockMock);

        IssuedServiceImpl issuedService = new IssuedServiceImpl(issuedRepository, couponRepository,
            redisConfigMock, cacheManager);

        when(issuedRepository.findByCouponIdAndUser(couponId, user)).thenReturn(issuedCoupons);

        // when, then
        assertThrows(IllegalStateException.class, () -> issuedService.issueCoupon(couponId, user));
    }

    @Test
    @DisplayName("쿠폰 발급 실패 - 쿠폰 없음")
    void issueCoupon_CouponNotFound() {
        // given
        Long couponId = 100L;
        User user = new User(100L, "customer", "CONSUMER");

        RedisConfig redisConfigMock = Mockito.mock(RedisConfig.class);
        RedissonClient redissonClientMock = Mockito.mock(RedissonClient.class);
        RLock rLockMock = Mockito.mock(RLock.class);

        Mockito.when(redisConfigMock.redissonClient()).thenReturn(redissonClientMock);
        Mockito.when(redissonClientMock.getLock(anyString())).thenReturn(rLockMock);

        IssuedServiceImpl issuedService = new IssuedServiceImpl(issuedRepository, couponRepository,
            redisConfigMock, cacheManager);

        // when, then
        assertThrows(NotFoundException.class, () -> issuedService.issueCoupon(couponId, user));
    }

    @Test
    @DisplayName("쿠폰 발급 실패 - 재고 없음")
    void issueCoupon_NoStock() {
        // given
        Long couponId = 100L;
        User user = new User(100L, "customer", "CONSUMER");

        Coupon coupon = new Coupon(couponId, "Test Coupon", 0.1D, LocalDateTime.now(),
            Deleted.UNDELETE, 100L);
        when(issuedRepository.findByCouponIdAndUser(couponId, user)).thenReturn(new ArrayList<>());
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

        RedisConfig redisConfigMock = Mockito.mock(RedisConfig.class);
        RedissonClient redissonClientMock = Mockito.mock(RedissonClient.class);
        RLock rLockMock = Mockito.mock(RLock.class);

        Mockito.when(redisConfigMock.redissonClient()).thenReturn(redissonClientMock);
        Mockito.when(redissonClientMock.getLock(anyString())).thenReturn(rLockMock);

        IssuedServiceImpl issuedService = new IssuedServiceImpl(issuedRepository, couponRepository,
            redisConfigMock, cacheManager);

        // when, then
        assertThrows(IllegalStateException.class, () -> issuedService.issueCoupon(couponId, user));
    }

    @AfterEach
    void tearDown() {
        reset(redisConfig);
    }

}

