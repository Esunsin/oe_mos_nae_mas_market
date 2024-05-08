package cheolppochwippo.oe_mos_nae_mas_market.domain.user.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity.Store;
import cheolppochwippo.oe_mos_nae_mas_market.domain.store.repository.StoreRepository;
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
    private final StoreRepository storeRepository;

    @Async
    @Override
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
    @Override
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

    //유저 테스트를 위한 회원가입 본래는 사용자가 판매자 신청을 넣고 admin이 신청을 받는 형태이지만 사용자 테스트때 편의성을
    //위해 버튼으로 판매자->구매자를 변경하기 위해 store를 미리 만들어주는 회원가입
    @Async
    @Override
    public CompletableFuture<UserResponse> signupByUserTest(UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new DuplicateUsernameException(
                messageSource.getMessage("duplicate.username", null, Locale.KOREA));
        }

        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest, encodedPassword);

        User savedUser = userRepository.save(user);
        storeRepository.save(new Store(new StoreRequest(user.getUsername()+"님의상점",""),user));
        return CompletableFuture.completedFuture(new UserResponse(savedUser));
    }

    @Override
    public UserResponse showMypage(User user) {
        User findUser = foundUser(user);

        return new UserResponse(findUser);
    }

    @Override
    @Transactional
    public UserResponse updateMypage(UserUpdateRequest userRequest, User user) {
        User findUser = foundUser(user);
        findUser.update(userRequest);
        return new UserResponse(findUser);
    }

    @Override
    @Transactional
    public UserResponse roleUpdate(User user) {
        User findUser = foundUser(user);
        if(user.getRole()==RoleEnum.SELLER){
            findUser.changeRoleToConsumer();
        }
        else if(user.getRole()==RoleEnum.CONSUMER){
            findUser.changeRoleToSeller();
        }
        return new UserResponse(findUser);
    }

    private User foundUser(User user) {
        return userRepository.findById(user.getId())
            .orElseThrow(() -> new NoEntityException(
                messageSource.getMessage("noEntity.user", null, Locale.KOREA)));
    }
}
