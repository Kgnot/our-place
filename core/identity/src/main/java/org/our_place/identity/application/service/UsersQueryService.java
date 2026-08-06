package org.our_place.identity.application.service;

import lombok.RequiredArgsConstructor;
import org.our_place.identity.application.service.dto.UserLoginDto;
import org.our_place.identity.infra.repository.UsersLoginRepository;
import org.our_place.shared.utils.Result;
import org.our_place.shared.utils.ResultIssue;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersQueryService {


    private final UsersLoginRepository usersLoginRepository;

    public Result<UserLoginDto> findByEmail(String email) {
        var user = usersLoginRepository.findByEmail(email);
        if (user != null) {
            return Result.ok(new UserLoginDto(
                    user.getId(),
                    user.getEmail(),
                    user.getProfile().getFirstName()
            ));
        }
        return Result.fail(new ResultIssue(
                "USER_NOT_FOUND",
                "User not found",
                ResultIssue.Severity.WARNING));
    }

    public Result<UserLoginDto> findNameUserById(UUID id) {
        var user = usersLoginRepository.findById(id);
        return user.map(usersLogin -> Result.ok(new UserLoginDto(
                usersLogin.getId(),
                usersLogin.getEmail(),
                usersLogin.getProfile().getFirstName()
        ))).orElseGet(() -> Result.fail(new ResultIssue(
                "USER_NOT_FOUND",
                "User not found",
                ResultIssue.Severity.WARNING)));
    }

}
