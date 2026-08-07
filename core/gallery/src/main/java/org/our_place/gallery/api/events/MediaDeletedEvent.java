package org.our_place.gallery.api.events;

import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

@SharedDomain(description = "Represents an event where a media item is deleted.")
// TODO, posiblemente buscar el id del room o de los que hacen parte de un room para enviar gmail
public record MediaDeletedEvent(
        UUID mediaId,
        UUID roomId
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
        return "media.deleted";
    }

    @Override
    public EventScope scope() {
        return EventScope.BOTH;
    }
}
