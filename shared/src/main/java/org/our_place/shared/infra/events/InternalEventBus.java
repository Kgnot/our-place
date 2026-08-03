package org.our_place.shared.infra.events;

import lombok.extern.slf4j.Slf4j;
import org.our_place.shared.application.bus.EventBus;
import org.our_place.shared.application.events.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Qualifier("internalEventBus")
@Component
@Slf4j
public class InternalEventBus implements EventBus {

    private final ApplicationEventPublisher applicationEventPublisher;

    public InternalEventBus(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        log.debug("Publishing event - internal : {}", event);
        this.applicationEventPublisher.publishEvent(event);
    }
}
