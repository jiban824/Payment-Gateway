package com.example.Payment.Gateway.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventBus {
    private final List<Consumer<TransactionEvent>> subscribers = new CopyOnWriteArrayList<>();

    public void subscribe(Consumer<TransactionEvent> subscriber) {
        subscribers.add(subscriber);
        log.info("[EventBus] New subscriber registered. Total subscribers: {}", subscribers.size());
    }

    public void publish(TransactionEvent event) {
        log.info("[EventBus] Publishing event | type={} txnId={} eventId={}",
                event.getEventType(), event.getTransactionId(), event.getEventId());
        for (Consumer<TransactionEvent> subscriber : subscribers) {
            try {
                subscriber.accept(event);
            } catch (Exception e) {
                log.error("[EventBus] Subscriber error for event {}: {}", event.getEventId(), e.getMessage());
            }
        }
    }
}
