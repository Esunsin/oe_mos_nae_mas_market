package cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {
    private String phoneNumber;

    private boolean consent;
}
