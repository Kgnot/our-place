package org.our_place.identity.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.identity.application.usecase.command.LoginCommand;
import org.our_place.identity.application.usecase.output.LoginOutput;
import org.our_place.identity.domain.entity.UsersLogin;
import org.our_place.identity.domain.exception.InvalidCredentialsException;
import org.our_place.identity.domain.vo.UserStatus;
import org.our_place.identity.persistence.repository.UserRoleRepository;
import org.our_place.identity.persistence.repository.UsersLoginRepository;
import org.our_place.identity.provider.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginUseCase implements UseCase<LoginCommand, LoginOutput> {
    private final UsersLoginRepository usersLoginRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional(noRollbackFor = InvalidCredentialsException.class)
    public LoginOutput execute(LoginCommand command) {
        UsersLogin user = usersLoginRepository.findByEmail(command.email());
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        user.assertCanLogin(); // lanza UserLockedException si aplica

        if (!passwordEncoder.matches(command.password(), user.getPasswordHash())) {
            user.registerFailedLogin(); // dirty checking guarda esto al hacer commit
            throw new InvalidCredentialsException();
        }

        user.registerSuccessfulLogin(); // checkeo rapido

        List<String> roles = userRoleRepository.findByUsersLogin_Id(user.getId())
                .stream()
                .map(ur -> ur.getRole().getCode())
                .toList();

        String accessToken = jwtProvider.generateAccessToken(user.getId(), null, user.getEmail(), roles);
        String refreshToken = jwtProvider.generateRefreshToken(user.getId(), null);

        return new LoginOutput(user.getId(), accessToken, refreshToken);
    }

}
