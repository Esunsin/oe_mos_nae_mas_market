package cheolppochwippo.oe_mos_nae_mas_market.payment.service;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum.COMPLETE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.repository.DeliveryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.order.repository.OrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentCancelRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.dto.PaymentResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.Payment;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.repository.PaymentRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.service.PaymentServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.entity.TotalOrder;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.TossPaymentConfig;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.PriceMismatchException;
import com.querydsl.core.Tuple;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

	@Mock
	PaymentRepository paymentRepository;

	@Mock
	DeliveryRepository deliveryRepository;

	@Mock
	TotalOrderRepository totalOrderRepository;

	@Mock
	IssuedRepository issuedRepository;

	@Mock
	OrderRepository orderRepository;

	@Mock
	TossPaymentConfig tossPaymentConfig;

	@Mock
	ProductServiceImpl productService;

	@Mock
	MessageSource messageSource;

	@InjectMocks
	PaymentServiceImpl paymentService;


	private User testUser() {
		return new User(1L, "test", "12345678", RoleEnum.SELLER,"01012345678",false);
	}

	private TotalOrder testTotalOrder(User user) {
		return new TotalOrder(1L, 50L, 10L, 50L, 0L, "테스트", "서울", 0L, Deleted.UNDELETE, user,
			PaymentStatementEnum.WAIT, "testId");
	}

	private Payment testPayment(TotalOrder totalOrder) {
		return new Payment(1L, 50L, "test", "testId", true, "testKey", false, "없음", COMPLETE,
			totalOrder);
	}

	@Test
	@DisplayName("결제 성공 테스트")
	void paymentSuccessTest() {
		//given
		TotalOrder totalOrder = testTotalOrder(testUser());
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(500L);
		paymentRequest.setPaymentKey("testKey");
		paymentRequest.setOrderId("testId");
		//given-then
		paymentService.successPayment(totalOrder, paymentRequest);
	}

	@Test
	@DisplayName("결제 취소 성공 테스트")
	void paymentCancelSuccessTest() {
		//given
		TotalOrder totalOrder = testTotalOrder(testUser());
		Payment payment = testPayment(totalOrder);
		PaymentCancelRequest paymentRequest = new PaymentCancelRequest();
		//given-then
		paymentService.successCancelPayment(payment, paymentRequest);
	}

	@Test
	@DisplayName("결제 실패 테스트")
	void paymentFailTest() {
		//given
		TotalOrder totalOrder = testTotalOrder(testUser());
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(500L);
		paymentRequest.setPaymentKey("testKey");
		paymentRequest.setOrderId("testId");
		//given-then
		paymentService.failPayment(totalOrder, paymentRequest);
	}

	@Test
	@DisplayName("결제 검증 테스트")
	void checkPaymentTest() {
		//given
		User user = testUser();
		TotalOrder totalOrder = testTotalOrder(user);
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(50L);
		paymentRequest.setPaymentKey("testKey");
		paymentRequest.setOrderId("testId");
		when(totalOrderRepository.findTotalOrderByUndeleted(user)).thenReturn(
			Optional.of(totalOrder));
		//when
		TotalOrder response = paymentService.checkPayment(user, paymentRequest);
		//then
		assertEquals(response.getOrderName(), totalOrder.getOrderName());
		assertEquals(response.getAddress(), totalOrder.getAddress());
	}

	@Test
	@DisplayName("결제 검증 테스트 - NOTFOUND")
	void checkPaymentNotFoundTest() {
		//given
		User user = testUser();
		TotalOrder totalOrder = testTotalOrder(user);
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(50L);
		paymentRequest.setPaymentKey("testKey");
		paymentRequest.setOrderId("testId");
		when(totalOrderRepository.findTotalOrderByUndeleted(user)).thenReturn(Optional.empty());
		//when
		Exception exception = assertThrows(NoEntityException.class,
			() -> paymentService.checkPayment(user, paymentRequest));
	}

	@Test
	@DisplayName("결제 검증 테스트 - BAD REQUEST")
	void checkPaymentBadReqeustTest() {
		//given
		User user = testUser();
		TotalOrder totalOrder = testTotalOrder(user);
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(50L);
		paymentRequest.setPaymentKey("testKey");
		paymentRequest.setOrderId("test");
		when(totalOrderRepository.findTotalOrderByUndeleted(user)).thenReturn(
			Optional.of(totalOrder));
		//when
		Exception exception = assertThrows(PriceMismatchException.class,
			() -> paymentService.checkPayment(user, paymentRequest));
	}

	@Test
	@DisplayName("결제 취소 검증 테스트")
	void checkCanselPaymentTest() {
		//given
		User user = testUser();
		TotalOrder totalOrder = testTotalOrder(user);
		Payment payment = testPayment(totalOrder);
		PaymentCancelRequest paymentRequest = new PaymentCancelRequest();
		paymentRequest.setPaymentKey("testKey");
		paymentRequest.setCancelReason("test");
		when(paymentRepository.findPaymentKey(paymentRequest.getPaymentKey())).thenReturn(
			Optional.of(payment));
		//when
		Payment response = paymentService.checkCancelPayment(user, paymentRequest);
		//then
		assertEquals(response.getAmount(), totalOrder.getPriceAmount());
		assertEquals(response.getPaymentKey(), paymentRequest.getPaymentKey());
	}

	@Test
	@DisplayName("결제 취소 검증 테스트 - NOTFOUND")
	void checkCanselPaymentNotFoundTest() {
		//given
		User user = testUser();
		TotalOrder totalOrder = testTotalOrder(user);
		Payment payment = testPayment(totalOrder);
		PaymentCancelRequest paymentRequest = new PaymentCancelRequest();
		paymentRequest.setPaymentKey("testKey");
		paymentRequest.setCancelReason("test");
		when(paymentRepository.findPaymentKey(paymentRequest.getPaymentKey())).thenReturn(Optional.empty());
		//when
		Exception exception = assertThrows(NoEntityException.class,
			() -> paymentService.checkCancelPayment(user, paymentRequest));
	}

	@Test
	@DisplayName("결제 취소 검증 테스트 - FORBIDDEN")
	void checkCanselPaymentForBiddenTest() {
		//given
		User user = testUser();
		TotalOrder totalOrder = testTotalOrder(new User(5L, "test", "12345678", RoleEnum.SELLER,"01012345678",false));
		Payment payment = testPayment(totalOrder);
		PaymentCancelRequest paymentRequest = new PaymentCancelRequest();
		paymentRequest.setPaymentKey("testKey");
		paymentRequest.setCancelReason("test");
		when(paymentRepository.findPaymentKey(paymentRequest.getPaymentKey())).thenReturn(
			Optional.of(payment));
		//when
		Exception exception = assertThrows(NoPermissionException.class,
			() -> paymentService.checkCancelPayment(user, paymentRequest));
	}

	@Test
	@DisplayName("결제 조회 테스트")
	void getPaymentTest() {
		//given
		User user = testUser();
		TotalOrder totalOrder = testTotalOrder(user);
		Payment payment = testPayment(totalOrder);
		Long paymentId = 3L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
		//when
		PaymentResponse response = paymentService.getPayment(user, paymentId);
		//then
		assertEquals(response.getAmount(), payment.getAmount());
		assertEquals(response.getPaymentKey(), payment.getPaymentKey());
	}

	@Test
	@DisplayName("결제 조회 테스트 - NOTFOUND")
	void getPaymentNotFoundTest() {
		//given
		User user = testUser();
		TotalOrder totalOrder = testTotalOrder(user);
		Payment payment = testPayment(totalOrder);
		Long paymentId = 3L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());
		//when
		Exception exception = assertThrows(NoEntityException.class,
			() -> paymentService.getPayment(user, paymentId));
	}

	@Test
	@DisplayName("결제 조회 테스트 - FORBIDDEN")
	void getPaymentForBiddenTest() {
		//given
		User user = testUser();
		TotalOrder totalOrder = testTotalOrder(new User(5L, "test", "12345678", RoleEnum.SELLER,"01012345678",false));
		Payment payment = testPayment(totalOrder);
		Long paymentId = 3L;
		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
		//when
		Exception exception = assertThrows(NoPermissionException.class,
			() -> paymentService.getPayment(user, paymentId));
	}

}
