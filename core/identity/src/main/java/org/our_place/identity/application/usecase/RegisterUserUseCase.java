package org.our_place.identity.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.our_place.identity.api.event.UserCreatedEvent;
import org.our_place.identity.application.usecase.command.RegisterUserCommand;
import org.our_place.identity.application.usecase.output.RegisterUserOutput;
import org.our_place.identity.domain.entity.LkpUserStatus;
import org.our_place.identity.domain.entity.Profile;
import org.our_place.identity.domain.entity.UsersLogin;
import org.our_place.identity.domain.exception.EmailAlreadyRegisteredException;
import org.our_place.identity.persistence.repository.LkpUserStatusRepository;
import org.our_place.identity.persistence.repository.ProfileRepository;
import org.our_place.identity.persistence.repository.UsersLoginRepository;
import org.our_place.shared.application.bus.EventBus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterUserUseCase implements UseCase<RegisterUserCommand, RegisterUserOutput> {

    private static final String STATUS_PENDING_VERIFICATION = "pending_verification";
    private static final String PROVIDER_LOCAL = "local";

    private final UsersLoginRepository usersLoginRepository;
    private final ProfileRepository profileRepository; // Added to persist Profile explicitly
    private final LkpUserStatusRepository lkpUserStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventBus eventBus;
    private final Clock clock; // Injectable clock for testable time sourcing

    @Override
    @Transactional
    public RegisterUserOutput execute(RegisterUserCommand command) {
        if (usersLoginRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyRegisteredException(command.email());
        }

        LkpUserStatus pending = lkpUserStatusRepository.getReferenceById(STATUS_PENDING_VERIFICATION);

        UsersLogin usersLogin = UsersLogin.create(
                command.email(),
                passwordEncoder.encode(command.password()),
                PROVIDER_LOCAL,
                pending
        );

        //persist user
        usersLoginRepository.save(usersLogin);

        Profile profile = new Profile();
        profile.setUsersLogin(usersLogin);
        profile.setFirstName(command.firstName());
        profile.setLastName(command.lastName());
        profile.setCreatedAt(OffsetDateTime.now(clock));
        //persiste profile
        profileRepository.save(profile);

        eventBus.publish(new UserCreatedEvent(
                usersLogin.getId(),
                usersLogin.getEmail(),
                command.firstName(),
                List.of()
        ));

        return new RegisterUserOutput(usersLogin.getId(), usersLogin.getEmail(), STATUS_PENDING_VERIFICATION);
    }
}