package org.our_place.shared.infra.events;

import lombok.extern.slf4j.Slf4j;
import org.our_place.shared.application.bus.EventBus;
import org.our_place.shared.application.events.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("externalEventBus")
public class ExternalEventBus implements EventBus {

    @Override
    public void publish(DomainEvent event) {
        log.debug("Publishing event - external: {}", event);
        log.error("External event bus is not implemented yet. Event: {}", event);
    }
}
