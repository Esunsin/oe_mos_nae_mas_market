package cheolppochwippo.oe_mos_nae_mas_market.domain.user.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserResponse;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<UserResponse> signup(UserRequest userRequest);

    CompletableFuture<UserResponse> login(UserRequest userRequest);
}
