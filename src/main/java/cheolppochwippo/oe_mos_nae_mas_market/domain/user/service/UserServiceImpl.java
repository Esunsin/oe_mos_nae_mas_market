package cheolppochwippo.oe_mos_nae_mas_market.domain.user.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.RoleUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserUpdateRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.DuplicateUsernameException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InvalidCredentialsException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Async
    public CompletableFuture<UserResponse> signup(UserRequest userRequest) {

        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new DuplicateUsernameException(
                messageSource.getMessage("duplicate.username", null, Locale.KOREA));
        }

        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest, encodedPassword);

        User savedUser = userRepository.save(user);
        return CompletableFuture.completedFuture(new UserResponse(savedUser));
    }

    @Async
    public CompletableFuture<UserResponse> login(UserRequest userRequest) {
        User findUser = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(
            () -> new InvalidCredentialsException(
                messageSource.getMessage("invalid.credentials.username", null, Locale.KOREA))
        );
        if (!passwordEncoder.matches(userRequest.getPassword(), findUser.getPassword())) {
            throw new InvalidCredentialsException(
                messageSource.getMessage("invalid.credentials.password", null, Locale.KOREA));
        }

        return CompletableFuture.completedFuture(new UserResponse(findUser));
    }

    @Override
    public UserResponse showMypage(User user) {
        User findUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new NoEntityException(
                messageSource.getMessage("noEntity.user", null, Locale.KOREA)));


        return new UserResponse(findUser);

    }

    @Override
    @Transactional
    public UserResponse updateMypage(UserUpdateRequest userRequest, User user) {
        User findUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new NoEntityException(
                messageSource.getMessage("noEntity.user", null, Locale.KOREA)));


        findUser.update(userRequest);
        return new UserResponse(findUser);
    }

    @Override
    public UserResponse roleUpdate( User user) {
        if (user.getRole() == RoleEnum.CONSUMER) {
            user.changeRoleToSeller();
            User updatedUser = userRepository.save(user);
            return new UserResponse(updatedUser);
        } else {
            throw new IllegalArgumentException("역할 변경이 불가능한 유저입니다.");
        }
    }
}
