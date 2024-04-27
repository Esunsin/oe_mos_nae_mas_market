package cheolppochwippo.oe_mos_nae_mas_market.domain.search.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.search.document.ProductDocument;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findByProductName(String keyword);

    void deleteByProductId(Long productId);
}
