package org.our_place.shared.infra.security;

import java.util.List;
import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        String email,
        List<String> roles
) {
}
