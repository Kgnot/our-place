package org.our_place.identity.api;

import org.our_place.common.shared.SharedApi;

import java.util.UUID;

@SharedApi(description = "API for managing identity-related operations")
public interface IdentityApi {

    UUID findUserIdByEmail(String email);

    String findNameUserById(UUID id);
}
