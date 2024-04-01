package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.repository.CouponRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.service.CouponService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.dto.IssuedResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.entity.Issued;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssuedServiceImpl implements IssuedService {

    private final IssuedRepository issuedRepository;
    private final CouponService couponService;
    private final CouponRepository couponRepository;

    @Override
    @Transactional
    public IssuedResponse issueCoupon(Long couponId, User user) {
        Map<Long, Boolean> issuedCouponMap = issuedRepository.findByCouponIdAndUser(couponId, user)
            .stream()
            .collect(Collectors.toMap(issued -> issued.getCoupon().getId(), issued -> true));

        if (issuedCouponMap.containsKey(couponId)) {
            throw new IllegalStateException("이미 쿠폰을 발급 받으셨습니다.");
        }
        Coupon coupon = couponService.findCoupon(couponId);

        if (coupon.getAmount() > 0) {
            coupon.decreaseAmount();
            couponRepository.save(coupon);

            Issued issuedCoupon = new Issued(coupon, user);
            issuedRepository.save(issuedCoupon);

            return new IssuedResponse(couponId, coupon.getCouponInfo(),
                issuedCoupon.getCreatedAt());
        } else {
            throw new IllegalArgumentException("남아있는 쿠폰이 없습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssuedResponse> getIssuedCoupons(User user) {
        List<Issued> issuedCoupons = issuedRepository.findByUser(user);
        return issuedCoupons.stream()
            .map(IssuedResponse::new)
            .collect(Collectors.toList());
    }
}
