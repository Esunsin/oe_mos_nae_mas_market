package cheolppochwippo.oe_mos_nae_mas_market.domain.issued;

import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.dto.CouponRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.coupon.entity.Coupon;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service.IssuedService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.service.IssuedServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class IssuedServiceTest {

    @Autowired
    IssuedServiceImpl issuedService;

    @Test
    @DisplayName("coupon decrease")
    void decreaseCouponAmount(){
        //given
        CouponRequest couponRequest = new CouponRequest("aaa",2000D, LocalDateTime.now(),1000L);
        Coupon coupon = new Coupon(couponRequest);
        //when
        IntStream.range(0,1000).parallel().forEach(i -> issuedService.decreaseCouponAmount(coupon));
        //then
        assertEquals(0,coupon.getAmount());
    }

}
