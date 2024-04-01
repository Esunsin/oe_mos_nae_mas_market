package cheolppochwippo.oe_mos_nae_mas_market.domain.store.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.service.StoreService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<CommonResponse<StoreResponse>> creatStore(
        @RequestBody StoreRequest storeRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResponse createStore = storeService.createStore(storeRequest,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<StoreResponse>builder()
                .msg("create store complete!")
                .data(createStore)
                .build());
    }
    @PatchMapping("/stores/{storeId}")
    public ResponseEntity<CommonResponse<StoreResponse>> updateStore(
        @RequestBody StoreRequest storeRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResponse updateStore = storeService.updateStore(storeRequest,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<StoreResponse>builder()
                .msg("update store complete!")
                .data(updateStore)
                .build());
    }
}
