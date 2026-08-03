package org.our_place.identity.api;

import org.our_place.common.shared.SharedApi;
import org.our_place.shared.infra.security.AuthenticatedUser;

import java.util.List;
import java.util.UUID;

@SharedApi(description = "API for managing the security context of the application")
public interface SecurityContextApi {
    AuthenticatedUser getAuthenticatedUser();

    UUID getCurrentUserId();

    String getCurrentUserEmail();

    List<String> getCurrentUserRoles();

    boolean isUserInRole(String role);
}
