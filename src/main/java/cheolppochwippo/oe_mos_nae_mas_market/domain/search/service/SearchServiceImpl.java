package cheolppochwippo.oe_mos_nae_mas_market.domain.search.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.search.document.ProductDocument;
import cheolppochwippo.oe_mos_nae_mas_market.domain.search.repository.ProductSearchRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final ProductSearchRepository productSearchRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDocument> searchProduct(String keyword) {
        return productSearchRepository.findByProductName(keyword);
    }

}
