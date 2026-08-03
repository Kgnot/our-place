package org.our_place.shared.infra.events;

import lombok.extern.slf4j.Slf4j;
import org.our_place.shared.application.bus.EventBus;
import org.our_place.shared.application.events.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
public class RoutingEventBus implements EventBus {

    private final EventBus internal;
    private final EventBus external;

    public RoutingEventBus(@Qualifier("internalEventBus") EventBus internal,
                           @Qualifier("externalEventBus") EventBus external) {
        this.internal = internal;
        this.external = external;
    }

    @Override
    public void publish(DomainEvent event) {
        log.debug("Publishing - routing {}", event);
        switch (event.scope()) {
            case INTERNAL -> internal.publish(event);
            case EXTERNAL -> external.publish(event);
            case BOTH -> {
                internal.publish(event);
                external.publish(event);
            }
        }
    }
}
