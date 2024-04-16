package cheolppochwippo.oe_mos_nae_mas_market.payment.lock;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.service.PaymentServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentLockTest {

	@Autowired
	PaymentServiceImpl paymentService;


//	@Test
//	@DisplayName("분산락테스트")
//	void redis() throws InterruptedException {
//		int numThreads = 200;
//		CountDownLatch latch = new CountDownLatch(numThreads);
//		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
//		User user = new User(6L);
//		PaymentRequest paymentRequest = new PaymentRequest();
//		paymentRequest.setAmount(4700L);
//		paymentRequest.setOrderId("96de40ea-d4ab-4842-9919-20037a34a0c3");
//
//		for (int i = 0; i < numThreads; i++) {
//			int userId = i + 1; // 사용자 번호
//			executor.submit(() -> {
//				try {
//					paymentService.checkPayment(user, paymentRequest);
//					//productService.decreaseProductStock(order);
//				} catch (Exception e) {
//					System.out.println(e.getMessage());
//				} finally {
//					latch.countDown();
//				}
//			});
//		}
//
//		// 모든 스레드가 실행을 완료할 때까지 대기
//		latch.await(15, TimeUnit.SECONDS);
//
//	}


}
