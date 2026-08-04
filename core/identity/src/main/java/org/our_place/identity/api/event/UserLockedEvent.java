package org.our_place.identity.api.event;

import org.our_place.common.shared.SharedDomain;

import java.util.UUID;

@SharedDomain(description = "Event triggered when a user account gets locked due to failed login attempts")
public record UserLockedEvent(
        UUID userId,
        String email,
        int failedAttempts
) {
}