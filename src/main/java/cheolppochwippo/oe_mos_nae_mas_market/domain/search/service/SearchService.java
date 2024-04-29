package cheolppochwippo.oe_mos_nae_mas_market.domain.search.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.search.document.ProductDocument;
import java.util.List;

public interface SearchService {

    List<ProductDocument> searchProduct(String keyword);
}
