package cheolppochwippo.oe_mos_nae_mas_market.domain.store.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.service.StoreService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.userDetails.UserDetailsImpl;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        StoreResponse createStore = storeService.createStore(storeRequest, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<StoreResponse>builder()
                .msg("create store sign complete!")
                .data(createStore)
                .build());
    }

    @PatchMapping("/stores")
    public ResponseEntity<CommonResponse<StoreResponse>> updateStore(
        @RequestBody StoreRequest storeRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResponse updateStore = storeService.updateStore(storeRequest, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<StoreResponse>builder()
                .msg("update store complete!")
                .data(updateStore)
                .build());
    }

    @GetMapping("/stores")
    public ResponseEntity<CommonResponse<StoreResponse>> showStore(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResponse showStore = storeService.showStore(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<StoreResponse>builder()
                .msg("show store complete!")
                .data(showStore)
                .build());
    }


    @PutMapping("/stores/{storeId}/approve")
    public ResponseEntity<CommonResponse<StoreResponse>> approveStore(
        @PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResponse approveStore = storeService.approveStore(storeId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<StoreResponse>builder()
                .msg("approve create store complete!")
                .data(approveStore)
                .build());
    }

    //상점 승인 전 상점 목록
    @GetMapping("/stores/false")
    public ResponseEntity<CommonResponse<List<StoreResponse>>> showFalseStore(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<StoreResponse> approveStore = storeService.showFalseStore(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.< List<StoreResponse>>builder()
                .msg("show false stores complete!")
                .data(approveStore)
                .build());
    }

    //상점 승인 전 상점 목록
    @GetMapping("/stores/true")
    public ResponseEntity<CommonResponse<List<StoreResponse>>> showTrueStore(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<StoreResponse> approveStore = storeService.showTrueStore(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.< List<StoreResponse>>builder()
                .msg("show true stores complete!")
                .data(approveStore)
                .build());
    }


}
