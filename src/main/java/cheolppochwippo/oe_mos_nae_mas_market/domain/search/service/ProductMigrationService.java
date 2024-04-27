package cheolppochwippo.oe_mos_nae_mas_market.domain.search.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.product.entity.Product;
import cheolppochwippo.oe_mos_nae_mas_market.domain.product.repository.ProductRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.search.document.ProductDocument;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductMigrationService {
    private final ProductRepository productRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    public void migrateProductsToElasticsearch() {
        List<Product> products = productRepository.findAll();
        List<ProductDocument> productDocuments = products.stream()
            .map(this::convertToProductDocument)
            .collect(Collectors.toList());

        IndexCoordinates indexCoordinates = IndexCoordinates.of("products");

        productDocuments.forEach(productDocument -> {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(productDocument.getId());
            indexQuery.setObject(productDocument);
            elasticsearchTemplate.index(indexQuery, indexCoordinates);
        });
    }

    private ProductDocument convertToProductDocument(Product product) {
        return new ProductDocument(
            UUID.randomUUID().toString(),
            product.getId(),
            product.getProductName(),
            product.getInfo(),
            product.getRealPrice(),
            product.getDiscount(),
            product.getQuantity(),
            product.getDeleted()
        );
    }
}
