package wisoft.labservice.domain.auth.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wisoft.labservice.domain.auth.repository.UserRepository;
import wisoft.labservice.domain.auth.entity.User;
import wisoft.labservice.domain.common.exception.BusinessException;
import wisoft.labservice.domain.common.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean authenticate(final String username, final String password) {

        return authRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .isPresent();
    }

    public User getUser(final String username) {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public void signUp(final String username, final String password) {
        if (authRepository.existsByUsername(username)) {
            throw new BusinessException(ErrorCode.DUPLICATE_USER);
        }

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);

        authRepository.save(newUser);
    }
}
