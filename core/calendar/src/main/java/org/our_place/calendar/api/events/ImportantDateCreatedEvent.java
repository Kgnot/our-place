package org.our_place.calendar.api.events;


import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;


import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@SharedDomain(description = "Event triggered when a new important date is created")
public record ImportantDateCreatedEvent(UUID id, UUID roomId, String typeCode, LocalDate eventDate) implements DomainEvent {
    @Override public UUID eventId() { return UUID.randomUUID(); }
    @Override public Instant occurredAt() { return Instant.now(); }
    @Override public String key() { return "important_date.created"; }
    @Override public EventScope scope() { return EventScope.INTERNAL; }
}