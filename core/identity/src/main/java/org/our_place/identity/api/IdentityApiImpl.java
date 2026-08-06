package org.our_place.identity.api;

import lombok.RequiredArgsConstructor;
import org.our_place.identity.application.service.UsersQueryService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdentityApiImpl implements IdentityApi {

    private final UsersQueryService usersQueryService;


    @Override
    public UUID findUserIdByEmail(String email) {
        return usersQueryService.findByEmail(email).getValue().uuid();
    }

    @Override
    public String findNameUserById(UUID id) {
        return usersQueryService.findNameUserById(id).getValue().firstName();
    }
}
