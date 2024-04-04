package cheolppochwippo.oe_mos_nae_mas_market.payment.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.repository.DeliveryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository.OrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.repository.PaymentRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.TossPaymentConfig;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentServiceTest {

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	DeliveryRepository deliveryRepository;

	@Autowired
	TotalOrderRepository totalOrderRepository;

	@Autowired
	IssuedRepository issuedRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	TossPaymentConfig tossPaymentConfig;

	@Autowired
	RedissonClient redissonClient;

	@Autowired
	ProductServiceImpl productService;

	@Test
	@DisplayName("분산락테스트")
	void redis() throws InterruptedException {
		int numThreads = 100;
		CountDownLatch latch = new CountDownLatch(numThreads);
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		for (int i = 0; i < numThreads; i++) {
			int userId = i + 1; // 사용자 번호
			executor.submit(() -> {
				try {
				} finally {
					latch.countDown();
				}
			});
		}

		// 모든 스레드가 실행을 완료할 때까지 대기
		latch.await(15, TimeUnit.SECONDS);

	}


}
