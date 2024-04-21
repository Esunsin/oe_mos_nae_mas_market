package cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.QUser;
import cheolppochwippo.oe_mos_nae_mas_market.global.config.JpaConfig;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

	private final JpaConfig jpaConfig;

	@Override
	public List<String> getPhoneNumberFindByConsentTrue(){
		List<String> queue = jpaConfig.jpaQueryFactory()
			.select(QUser.user.phoneNumber)
			.from(QUser.user)
			.where(
				QUser.user.consent.eq(true)
			)
			.fetch();
		return queue;
	}
}
