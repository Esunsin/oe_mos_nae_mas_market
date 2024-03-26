package cheolppochwippo.oe_mos_nae_mas_market.user.service;

import cheolppochwippo.oe_mos_nae_mas_market.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.user.dto.UserResponse;

public interface UserService {
    UserResponse signup(UserRequest userRequest);

    UserResponse login(UserRequest userRequest);
}
