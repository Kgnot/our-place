package org.our_place.shared.application.events;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

    UUID eventId();

    Instant occurredAt();

    String key();

    EventScope scope();
}
