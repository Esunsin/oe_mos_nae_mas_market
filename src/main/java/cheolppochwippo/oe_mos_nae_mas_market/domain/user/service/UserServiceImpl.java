package cheolppochwippo.oe_mos_nae_mas_market.domain.user.service;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserResponse;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.ErrorCode;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NotFoundException;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public UserResponse signup(UserRequest userRequest){
        if(userRepository.findByUsername(userRequest.getUsername()).isPresent()){
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        User user = new User(userRequest.getUsername(), encodedPassword);

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    public UserResponse login(UserRequest userRequest) {
        User findUser = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(
                () -> new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );
        if (!passwordEncoder.matches(userRequest.getPassword(), findUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return new UserResponse(findUser);
    }
}
