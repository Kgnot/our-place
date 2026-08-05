package org.our_place.identity.api.event;

import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

@SharedDomain(description = "Event triggered when a user logs in successfully")
public record UserLoggedEvent(
        UUID userId,
        String name,
        String email
) implements DomainEvent {
    @Override
    public UUID eventId() {
        return UUID.randomUUID();
    }

    @Override
    public Instant occurredAt() {
        return Instant.now();
    }

    @Override
    public String key() {
        return "user.logged";
    }

    @Override
    public EventScope scope() {
        return EventScope.BOTH;
    }
}
