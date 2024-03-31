package cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository.CouponRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class CouponServiceImpl implements CouponService{
    private final CouponRepository couponRepository;

    @Override
    public CouponResponse createCoupon(CouponRequest couponRequest){
        Coupon coupon = new Coupon(couponRequest);
        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponResponse(savedCoupon);
    }

    @Override
    public List<CouponResponse> getCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        return coupons.stream()
            .map(CouponResponse::new)
            .collect(Collectors.toList());
    }

    @Override
    public CouponResponse updateCoupon(Long couponId, CouponRequest couponRequest){
        Coupon coupon = findCoupon(couponId);
        coupon.update(couponRequest);

        Coupon updatedCoupon = couponRepository.save(coupon);
        return new CouponResponse(updatedCoupon);
    }

    @Override
    public void deleteCoupon(Long couponId){
        Coupon coupon = findCoupon(couponId);
        couponRepository.delete(coupon);
    }


    private Coupon findCoupon(Long couponId) {
        return couponRepository.findById(couponId)
            .orElseThrow(() -> new NoSuchElementException("쿠폰을 찾을 수 없습니다."));
    }
}
