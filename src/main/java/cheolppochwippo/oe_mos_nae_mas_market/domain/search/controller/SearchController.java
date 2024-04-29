package cheolppochwippo.oe_mos_nae_mas_market.domain.search.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.search.document.ProductDocument;
import cheolppochwippo.oe_mos_nae_mas_market.domain.search.service.ProductMigrationService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.search.service.SearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final ProductMigrationService productMigrationService;

    @GetMapping("/products/_search")
    public List<ProductDocument> searchProduct(@RequestParam("keyword") String keyword){
        return searchService.searchProduct(keyword);
    }

    @PostMapping("/products/migration")
    public void migrateProductsToElasticsearch() {
        productMigrationService.migrateProductsToElasticsearch();
    }



}
