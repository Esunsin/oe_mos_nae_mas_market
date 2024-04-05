package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.Delivery;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.repository.DeliveryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.service.DeliveryService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.service.DeliveryServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @Mock
    DeliveryRepository deliveryRepository;

    DeliveryService deliveryService;

    @BeforeEach
    void before(){
        deliveryService = new DeliveryServiceImpl(deliveryRepository);
    }

    @Test
    @DisplayName("배송지 생성")
    void createDelivery(){
        // given
        User user = new User(100L, "customer", "CONSUMER");
        DeliveryRequest deliveryRequest = new DeliveryRequest("주소지 생성 테스트");

        Delivery savedDelivery = new Delivery(user, deliveryRequest);
        // when
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(savedDelivery);
        deliveryService.createDelivery(deliveryRequest, user);

        // then
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    @DisplayName("배송지 수정")
    void updateDelivery(){
        Long deliveryId = 100L;
        User user = new User(100L, "customer", "CONSUMER");
        DeliveryRequest deliveryRequest = new DeliveryRequest("주소지 생성 테스트");
        Delivery existingDelivery = new Delivery(100L, "주소지 수정 테스트", Deleted.UNDELETE, "testId",user);
        given(deliveryRepository.save(any(Delivery.class))).willReturn(existingDelivery);
        given(deliveryRepository.findById(100L)).willReturn(Optional.of(existingDelivery));

        // when
        DeliveryResponse updatedDelivery = deliveryService.updateDelivery(deliveryId, deliveryRequest, user);

        // then
        assertEquals(deliveryRequest.getAddress(), updatedDelivery.getAddress());
    }
}
