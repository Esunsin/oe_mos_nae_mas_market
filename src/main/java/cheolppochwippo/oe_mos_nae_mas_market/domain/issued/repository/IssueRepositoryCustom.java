package cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository;

import java.util.Optional;

public interface IssueRepositoryCustom {

	Optional<Double> getDiscountFindById(Long userId,Long issueId);

	void setDeletedFindById(Long issueId);

}
