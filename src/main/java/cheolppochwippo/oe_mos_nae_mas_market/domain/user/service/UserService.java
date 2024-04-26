package cheolppochwippo.oe_mos_nae_mas_market.domain.user.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<UserResponse> signup(UserRequest userRequest);

    CompletableFuture<UserResponse> login(UserRequest userRequest);

    UserResponse showMypage(User user);

    UserResponse updateMypage(UserUpdateRequest userRequest, User user);

    UserResponse roleUpdate(User user);
}
