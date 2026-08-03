package org.our_place.identity.config.util;


import org.our_place.identity.api.SecurityContextApi;
import org.our_place.shared.infra.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

// TODO, si no sirve volver a static todo
@Component
public class SecurityContextHelper implements SecurityContextApi {

    public AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated(authentication)) {
            return (AuthenticatedUser) Objects.requireNonNull(authentication.getPrincipal());
        }
        throw new SecurityException("No authenticated user found in security context");
    }

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated(authentication)) {
            return ((AuthenticatedUser) Objects.requireNonNull(authentication.getPrincipal())).userId();
        }
        throw new SecurityException("No authenticated user found in security context");
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated(authentication)) {
            return ((AuthenticatedUser) Objects.requireNonNull(authentication.getPrincipal())).email();
        }
        throw new SecurityException("No authenticated user found in security context");
    }

    public List<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated(authentication)) {
            return ((AuthenticatedUser) Objects.requireNonNull(authentication.getPrincipal())).roles();
        }
        throw new SecurityException("No authenticated user found in security context");
    }

    public boolean isUserInRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated(authentication)) {
            return ((AuthenticatedUser) Objects.requireNonNull(authentication.getPrincipal())).roles().contains(role);
        }
        return false;
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser;
    }
}
