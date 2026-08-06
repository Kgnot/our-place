package org.our_place.room.api.events;

import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

@SharedDomain(description = "Event triggered when a user joins a room after accepting an invitation")
public record MemberJoinedEvent(
        UUID roomId,
        UUID userLoginId,
        String roleCode
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
        return "room.member_joined";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}