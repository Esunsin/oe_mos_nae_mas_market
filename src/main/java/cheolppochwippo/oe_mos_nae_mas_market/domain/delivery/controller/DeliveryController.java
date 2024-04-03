package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.dto.DeliveryResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.service.DeliveryService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/delivery")
    public ResponseEntity<CommonResponse<DeliveryResponse>> createDelivery(
        @RequestBody DeliveryRequest deliveryRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        DeliveryResponse createdDelivery = deliveryService.createDelivery(deliveryRequest, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED.value())
            .body(CommonResponse.<DeliveryResponse>builder()
                .msg("delivery created complete!")
                .data(createdDelivery)
                .build());
    }

    @PatchMapping("/delivery/{deliveryId}")
    public ResponseEntity<CommonResponse<DeliveryResponse>> updateDelivery(
        @PathVariable Long deliveryId,
        @RequestBody DeliveryRequest deliveryRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        DeliveryResponse updatedDelivery = deliveryService.updateDelivery(deliveryId, deliveryRequest, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value())
            .body(CommonResponse.<DeliveryResponse>builder()
                .msg("delivery updated complete!")
                .data(updatedDelivery)
                .build());
    }

    @DeleteMapping("/delivery/{deliveryId}")
    public ResponseEntity<CommonResponse<String>> deleteDelivery(
        @PathVariable Long deliveryId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        deliveryService.deleteDelivery(deliveryId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value())
            .body(CommonResponse.<String>builder()
                .build());
    }

    @GetMapping("/delivery")
    public ResponseEntity<CommonResponse<List<DeliveryResponse>>> getDeliveries(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<DeliveryResponse> deliveryResponses = deliveryService.getDeliveries(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<List<DeliveryResponse>>builder()
                .msg("delivery get complete!")
                .data(deliveryResponses)
                .build());
    }

}
