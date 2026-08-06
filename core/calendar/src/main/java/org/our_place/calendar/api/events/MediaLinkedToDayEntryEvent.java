package org.our_place.calendar.api.events;


import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@SharedDomain(description = "Event triggered when a media file is linked to a day entry")
public record MediaLinkedToDayEntryEvent(UUID roomId, LocalDate entryDate, UUID mediaId) implements DomainEvent {
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
        return "day_entry.media_linked";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}