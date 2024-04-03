package cheolppochwippo.oe_mos_nae_mas_market.domain.product.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductShowResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductService;
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
public class ProductController {

    private final ProductService productService;

    //상품추가
    @PostMapping("/stores/products")
    public ResponseEntity<CommonResponse<ProductResponse>> createProduct(
        @RequestBody ProductRequest productRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductResponse createProduct = productService.createProduct(productRequest,
            userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<ProductResponse>builder()
                .msg("create products complete!")
                .data(createProduct)
                .build());
    }


    //상품수정
    @PatchMapping("/stores/products/{productId}")
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(
        @PathVariable Long productId, @RequestBody ProductRequest productRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductResponse updateProduct = productService.updateProduct(productRequest, productId,
            userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<ProductResponse>builder()
                .msg("update products complete!")
                .data(updateProduct) //productId
                .build());
    }

    //상품조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<ProductShowResponse>> showProduct(@PathVariable Long productId) {
        ProductShowResponse showProduct = productService.showProduct(productId);
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<ProductShowResponse>builder()
                .msg("get products complete!")
                .data(showProduct)
                .build());
    }

    //상품 전체 조회
    @GetMapping("/products")
    public ResponseEntity<CommonResponse<List<ProductShowResponse>>> showAllProduct() {
        List<ProductShowResponse> showAllProduct = productService.showAllProduct();
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<List<ProductShowResponse>>builder()
                .msg("get all products complete!")
                .data(showAllProduct)
                .build());
    }

    //상품 삭제
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<ProductResponse>> deleteProduct(
        @PathVariable Long productId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductResponse deleteProduct = productService.deleteProduct(productId,userDetails.getUser()); //productId
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<ProductResponse>builder()
                .msg("delete products complete!")
                .data(deleteProduct)
                .build());
    }
}
