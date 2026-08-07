package org.our_place.gallery.api.events;

import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

@SharedDomain(description = "Evento que se dispara cuando un medio es subido exitosamente")
public record MediaUploadedEvent(
        UUID mediaId,
        UUID roomId,
        String r2Key,
        String code,
        String mimeType
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
        return "media.uploaded";
    }

    @Override
    public EventScope scope() {
        return EventScope.BOTH;
    }
}
