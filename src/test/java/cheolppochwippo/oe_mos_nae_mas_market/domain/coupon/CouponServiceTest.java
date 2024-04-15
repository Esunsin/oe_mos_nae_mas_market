package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository.CouponRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.service.CouponService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.service.CouponServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.SQSConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    CouponRepository couponRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    SQSConfig sqsConfig;

    @Mock
    MessageSource messageSource;

    CouponService couponService;

    @BeforeEach
    void before() {
        couponService = new CouponServiceImpl(couponRepository,userRepository,sqsConfig,messageSource);
    }

    @Test
    @DisplayName("쿠폰 생성")
    void createCoupon() {
        // given
        CouponRequest couponRequest = new CouponRequest("Coupon Test", 0.1D, LocalDateTime.now(), 100L);

        // when
        Coupon savedCoupon = new Coupon(couponRequest);
        when(couponRepository.save(any(Coupon.class))).thenReturn(savedCoupon);
        couponService.createCoupon(couponRequest);

        // then
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    @DisplayName("쿠폰 수정")
    void updateCoupon() {
        // given
        Long couponId = 100L;
        CouponRequest couponRequest = new CouponRequest("Coupon Test", 0.1D, LocalDateTime.now(), 100L);
        Coupon existingCoupon = new Coupon(100L, "Coupon", 0.1D, LocalDateTime.now(), Deleted.UNDELETE, 100L);
        given(couponRepository.save(any(Coupon.class))).willReturn(existingCoupon);
        given(couponRepository.findById(100L)).willReturn(Optional.of(existingCoupon));

        // when
        CouponResponse updatedCoupon = couponService.updateCoupon(couponId, couponRequest);

        // then
        assertEquals(updatedCoupon.getCouponInfo(), couponRequest.getCouponInfo());
    }
}
