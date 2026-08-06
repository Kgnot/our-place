package org.our_place.room.api.events;

import org.our_place.common.shared.SharedDomain;
import org.our_place.shared.application.events.DomainEvent;
import org.our_place.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

@SharedDomain
public record MemberRelationshipSetEvent(
        Member memberA,
        Member memberB,
        String relationship,
        java.time.LocalDate sinceDate
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
        return "member_relationship.set";
    }

    @Override
    public EventScope scope() {
        return EventScope.BOTH;
    }

    public record Member(
            String id,
            String name
    ) {
    }

}
