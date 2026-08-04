package org.our_place.identity.api.event;

import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@SharedDomain(description = "Event triggered when a new user is created")
public record UserCreatedEvent(
        UUID userId,
        String email,
        String name,
        List<String> roles
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
        return "user.created";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}
