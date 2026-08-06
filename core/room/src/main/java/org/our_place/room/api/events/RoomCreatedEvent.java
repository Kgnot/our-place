package org.our_place.room.api.events;

import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

@SharedDomain(description = "Event triggered when a new room is created")
public record RoomCreatedEvent(
        UUID roomId,
        UUID ownerUserId,
        String relationshipTypeCode
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
        return "room.created";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}