package cheolppochwippo.oe_mos_nae_mas_market.global.redissonLockAoop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 이 MethodAop -> 메서드 타입에 적용하는 것을 지정
@Retention(RetentionPolicy.RUNTIME) // 런타임시
public @interface RedissonLockAnnotation{
}
