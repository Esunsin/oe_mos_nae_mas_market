package cheolppochwippo.oe_mos_nae_mas_market.domain.user.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.ErrorCode;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.DuplicateUsernameException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InvalidCredentialsException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NotFoundException;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Async
    public CompletableFuture<UserResponse> signup(UserRequest userRequest){

        if(userRepository.findByUsername(userRequest.getUsername()).isPresent()){
            throw new DuplicateUsernameException(messageSource.getMessage("duplicate.username", null, Locale.KOREA));
        }

        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest.getUsername(), encodedPassword, userRequest.getRole(),userRequest.getPhoneNumber(),userRequest.isConsent());

        User savedUser = userRepository.save(user);
        return CompletableFuture.completedFuture(new UserResponse(savedUser));
    }

    @Async
    public CompletableFuture<UserResponse> login(UserRequest userRequest) {
        User findUser = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(
                () -> new InvalidCredentialsException(messageSource.getMessage("invalid.credentials.username", null, Locale.KOREA))
        );
        if (!passwordEncoder.matches(userRequest.getPassword(), findUser.getPassword())) {
            throw new InvalidCredentialsException(messageSource.getMessage("invalid.credentials.password", null, Locale.KOREA));
        }

        return CompletableFuture.completedFuture(new UserResponse(findUser));
    }
}
