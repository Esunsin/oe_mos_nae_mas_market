package cheolppochwippo.oe_mos_nae_mas_market.user.dto;

import cheolppochwippo.oe_mos_nae_mas_market.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private String username;

    public UserResponse(User user) {
        this.username = user.getUsername();
    }
}

