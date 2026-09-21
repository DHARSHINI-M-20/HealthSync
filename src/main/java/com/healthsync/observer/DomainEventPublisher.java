package com.healthsync.observer;
import java.util.ArrayList;
import java.util.List;
/** Subject broadcasting domain events to independent subscribers. */
public class DomainEventPublisher {
    private final List<DomainEventListener> listeners = new ArrayList<>();
    public void subscribe(DomainEventListener listener) { listeners.add(listener); }
    public void publish(DomainEvent event) { listeners.forEach(listener -> listener.onEvent(event)); }
    // TODO: Add unsubscribe and resilient listener error handling.
}
