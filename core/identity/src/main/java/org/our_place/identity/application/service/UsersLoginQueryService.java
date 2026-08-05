package org.our_place.identity.application.service;

import lombok.RequiredArgsConstructor;
import org.our_place.identity.application.service.dto.UserProfileDto;
import org.our_place.identity.domain.entity.UsersLogin;
import org.our_place.identity.domain.exception.UserNotFoundException;
import org.our_place.identity.infra.repository.UsersLoginRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersLoginQueryService {

    private final UsersLoginRepository usersLoginRepository;

    public UserProfileDto getById(UUID userId) {
        UsersLogin user = usersLoginRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return new UserProfileDto(
                user.getId(),
                user.getEmail(),
                user.getProfile().getFirstName(),
                user.getProfile().getLastName(),
                user.getStatus().getCode()
        );
    }

    public boolean emailExists(String email) {
        return usersLoginRepository.existsByEmail(email);
    }
}