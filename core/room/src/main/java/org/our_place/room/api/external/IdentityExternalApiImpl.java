package org.our_place.room.api.external;

import lombok.RequiredArgsConstructor;
import org.our_place.identity.api.IdentityApi;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdentityExternalApiImpl implements IdentityExternalApi {

    private final IdentityApi identityApi;


    @Override
    public UUID findUserLoginIdByEmail(String email) {
        return identityApi.findUserIdByEmail(email);
    }

    @Override
    public String findNameUserById(UUID id) {
        return identityApi.findNameUserById(id);
    }
}
