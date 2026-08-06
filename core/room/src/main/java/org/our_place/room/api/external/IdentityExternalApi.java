package org.our_place.room.api.external;

import java.util.UUID;

public interface IdentityExternalApi {

    UUID findUserLoginIdByEmail(String email);

    String findNameUserById(UUID id);


}
