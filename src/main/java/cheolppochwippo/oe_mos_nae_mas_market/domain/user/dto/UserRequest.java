package cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String password;
}
