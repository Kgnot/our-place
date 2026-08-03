package org.our_place.shared.application.bus;

import org.our_place.shared.application.events.DomainEvent;

public interface EventBus {

    void publish(DomainEvent event);
}
