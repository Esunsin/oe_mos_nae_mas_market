package cheolppochwippo.oe_mos_nae_mas_market.domain.product.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.dto.ProductResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.service.ProductService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.service.UserService;
import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
        @RequestBody ProductRequest productRequest) {
        ProductResponse createProduct = productService.createProduct(productRequest); //productId
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<ProductResponse>builder()
                .msg("create products complete!")
                .data(createProduct)
                .build());
    }


    //상품수정
    @PatchMapping("/stores/products/{productId}")
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(
        @PathVariable Long productId, @RequestBody ProductRequest productRequest) {
        ProductResponse updateProduct = productService.updateProduct(productRequest); //productId
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<ProductResponse>builder()
                .msg("update products complete!")
                .data(updateProduct) //productId
                .build());
    }

    //상품조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<ProductResponse>> showProduct(@PathVariable Long productId,
        @RequestBody ProductRequest productRequest) {
        ProductResponse showProduct = productService.showProduct(productRequest); //productId
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<ProductResponse>builder()
                .msg("get products complete!")
                .data(showProduct)
                .build());
    }

    //상품 전체 조회
    @GetMapping("/products")
    public ResponseEntity<CommonResponse<ProductResponse>> showAllProduct(
        @RequestBody ProductRequest productRequest) {
        ProductResponse showAllProduct = productService.showAllProduct(productRequest);
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<ProductResponse>builder()
                .msg("get all products complete!")
                .data(showAllProduct)
                .build());
    }
    //상품 삭제
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<ProductResponse>> deleteProduct(
        @RequestBody ProductRequest productRequest) {
        ProductResponse deleteProduct = productService.deleteProduct(productRequest); //productId
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(CommonResponse.<ProductResponse>builder()
                .msg("delete products complete!")
                .data(deleteProduct)
                .build());
    }
}
