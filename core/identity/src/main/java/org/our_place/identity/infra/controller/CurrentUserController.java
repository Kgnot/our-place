package org.our_place.identity.infra.controller;

import lombok.RequiredArgsConstructor;
import org.our_place.identity.application.service.GetMeUserService;
import org.our_place.identity.application.service.dto.GetMeUserDto;
import org.our_place.identity.application.usecase.UpdateUserProfileUseCase;
import org.our_place.identity.application.usecase.command.UpdateUserProfileCommand;
import org.our_place.identity.application.usecase.output.UpdateUserProfileOutput;
import org.our_place.identity.config.util.SecurityContextHelper;
import org.our_place.identity.infra.controller.request.UpdateUserProfileRequest;
import org.our_place.shared.utils.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class CurrentUserController {

    private final GetMeUserService getMeUserService;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final SecurityContextHelper  securityContextHelper;

    @GetMapping("/me")
    public ResponseEntity<GetMeUserDto> getMe() {
        var userId = securityContextHelper.getCurrentUserId();
        Result<GetMeUserDto> result = getMeUserService.getUserWithProfile(userId);
        
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserProfileRequest request) {
        var userId = securityContextHelper.getCurrentUserId();

        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                userId,
                request.firstName(),
                request.lastName(),
                request.avatarUrl(),
                request.birthDate(),
                request.timezone(),
                request.locale()
        );

        UpdateUserProfileOutput output = updateUserProfileUseCase.execute(command);
        return ResponseEntity.ok(output);
    }

}
