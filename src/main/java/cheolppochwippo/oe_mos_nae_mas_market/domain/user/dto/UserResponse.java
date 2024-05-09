package cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private String username;
    private Long userId;
    private RoleEnum role;
    private String phoneNumber;
    private boolean consent;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.userId = user.getId();
        this.role = user.getRole();
        this.phoneNumber = user.getPhoneNumber();
        this.consent = user.isConsent();
    }
}

