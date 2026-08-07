package org.our_place.gallery.api.events;

import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

@SharedDomain(description = "Comentario creado en una foto o video")
public record MediaCommentAddedEvent(
        UUID mediaId,
        UUID roomId,
        String comment,
        UUID userLoginId

) implements DomainEvent {
    @Override
    public UUID eventId() {
        return null;
    }

    @Override
    public Instant occurredAt() {
        return null;
    }

    @Override
    public String key() {
        return "";
    }

    @Override
    public EventScope scope() {
        return null;
    }
}
