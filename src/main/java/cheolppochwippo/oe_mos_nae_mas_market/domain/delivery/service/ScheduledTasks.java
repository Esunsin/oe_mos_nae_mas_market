package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final DeliveryServiceImpl deliveryService;


    @Scheduled(fixedDelay = 1800000)
    public void updateWaitToReady() {
        deliveryService.updateWaitToReady();
    }

    @Scheduled(fixedDelay = 1800000)
    public void updateReadyToGoing() {
        deliveryService.updateReadyToGoing();
    }

    @Scheduled(fixedDelay = 1800000)
    public void updateGoingToComplete() {
        deliveryService.updateGoingToComplete();
    }
}
