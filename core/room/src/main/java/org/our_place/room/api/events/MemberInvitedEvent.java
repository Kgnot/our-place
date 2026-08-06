package org.our_place.room.api.events;

import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

@SharedDomain(description = "Event triggered when a room member sends an invitation; used by " +
        "the notifications module to send the invite email AFTER_COMMIT")
public record MemberInvitedEvent(
        UUID roomId,
        String invitedEmail,
        String token,
        UUID invitedByUserId
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
        return "room.member_invited";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}