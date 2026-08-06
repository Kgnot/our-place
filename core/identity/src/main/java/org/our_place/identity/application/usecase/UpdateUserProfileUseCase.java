package org.our_place.identity.application.usecase;

import lombok.RequiredArgsConstructor;
import org.our_place.identity.application.usecase.command.UpdateUserProfileCommand;
import org.our_place.identity.application.usecase.output.UpdateUserProfileOutput;
import org.our_place.identity.domain.entity.Profile;
import org.our_place.identity.domain.entity.UsersLogin;
import org.our_place.identity.domain.exception.UserNotFoundException;
import org.our_place.identity.infra.repository.UsersLoginRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateUserProfileUseCase implements UseCase<UpdateUserProfileCommand, UpdateUserProfileOutput> {

    private final UsersLoginRepository usersLoginRepository;

    @Override
    @Transactional
    public UpdateUserProfileOutput execute(UpdateUserProfileCommand command) {
        var user = usersLoginRepository.findById(command.userId())
                .orElseThrow(UserNotFoundException::new);

        var profile = getProfile(command, user);

        usersLoginRepository.save(user);

        return new UpdateUserProfileOutput(
                user.getId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getAvatarUrl(),
                profile.getBirthDate(),
                profile.getTimezone(),
                profile.getLocale(),
                user.getUpdatedAt()
        );
    }

    private static Profile getProfile(UpdateUserProfileCommand command, UsersLogin user) {
        var profile = user.getProfile();

        // Actualizar solo los campos que no sean null
        if (command.firstName() != null) {
            profile.setFirstName(command.firstName());
        }
        if (command.lastName() != null) {
            profile.setLastName(command.lastName());
        }
        if (command.avatarUrl() != null) {
            profile.setAvatarUrl(command.avatarUrl());
        }
        if (command.birthDate() != null) {
            profile.setBirthDate(command.birthDate());
        }
        if (command.timezone() != null) {
            profile.setTimezone(command.timezone());
        }
        if (command.locale() != null) {
            profile.setLocale(command.locale());
        }
        return profile;
    }
}
