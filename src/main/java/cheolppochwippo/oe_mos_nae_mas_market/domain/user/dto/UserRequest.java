package cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequest {

	private String username;

	private String password;

	private String phoneNumber;

	private boolean consent = false;
}
