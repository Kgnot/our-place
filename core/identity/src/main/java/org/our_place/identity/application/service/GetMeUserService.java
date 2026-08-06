package org.our_place.identity.application.service;

import lombok.RequiredArgsConstructor;
import org.our_place.identity.application.service.dto.GetMeUserDto;
import org.our_place.identity.infra.repository.UsersLoginRepository;
import org.our_place.shared.utils.Result;
import org.our_place.shared.utils.ResultIssue;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMeUserService {

    private final UsersLoginRepository usersLoginRepository;

    public Result<GetMeUserDto> getUserWithProfile(UUID userId) {
        var user = usersLoginRepository.findById(userId);
        
        if (user.isEmpty()) {
            return Result.fail(new ResultIssue(
                    "USER_NOT_FOUND",
                    "User not found",
                    ResultIssue.Severity.WARNING));
        }

        var usersLogin = user.get();
        var profile = usersLogin.getProfile();

        return Result.ok(new GetMeUserDto(
                usersLogin.getId(),
                usersLogin.getEmail(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getAvatarUrl(),
                profile.getBirthDate(),
                profile.getTimezone(),
                profile.getLocale(),
                usersLogin.getStatus().getCode(),
                usersLogin.isMfaEnabled(),
                usersLogin.getCreatedAt(),
                usersLogin.getUpdatedAt()
        ));
    }
}
