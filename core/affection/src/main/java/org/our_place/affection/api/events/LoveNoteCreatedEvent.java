package org.our_place.affection.api.events;


import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

@SharedDomain(description = "Event triggered when a new love note is created")
public record LoveNoteCreatedEvent(
        UUID noteId,
        UUID roomId,
        UUID authorUserId,
        String typeCode
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
        return "love_note.created";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}