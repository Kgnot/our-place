package org.our_place.identity.api.event;

import org.our_place.common.shared.SharedDomain;

import java.util.List;
import java.util.UUID;

@SharedDomain(description = "Event triggered when a new user is created")
public record UserCreatedEvent(
        UUID userId,
        String email,
        String name,
        List<String> roles
) {
}
