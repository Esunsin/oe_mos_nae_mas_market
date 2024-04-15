package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity.Delivery;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.repository.DeliveryRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final MessageSource messageSource;

    @Override
    @Transactional
    public DeliveryResponse createDelivery(DeliveryRequest deliveryRequest, User user) {
        Delivery delivery = new Delivery(user, deliveryRequest);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return new DeliveryResponse(savedDelivery);
    }

    @Override
    @Transactional
    public DeliveryResponse updateDelivery(Long deliveryId, DeliveryRequest deliveryRequest, User user) {
        Delivery delivery = findDelivery(deliveryId);
        delivery.update(deliveryRequest);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return new DeliveryResponse(savedDelivery);
    }

    @Override
    @Transactional
    public void deleteDelivery(Long deliveryId, User user) {
        Delivery delivery = findDelivery(deliveryId);
        delivery.delete();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryResponse> getDeliveries(User user) {
        List<Delivery> deliveries = deliveryRepository.findDeliveryByUser(user);
        return deliveries.stream()
            .map(DeliveryResponse::new)
            .collect(Collectors.toList());
    }

    private Delivery findDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new NoEntityException(messageSource.getMessage("noEntity.delivery", null, Locale.KOREA)));
    }

}
