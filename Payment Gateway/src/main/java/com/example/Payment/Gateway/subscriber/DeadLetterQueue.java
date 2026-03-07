package com.example.Payment.Gateway.subscriber;

import com.example.Payment.Gateway.event.TransactionEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeadLetterQueue {
    
    public void enqueue(TransactionEvent event, Exception exception) {
        log.error("[DLQ] Event moved to dead letter queue | eventId={} eventType={} error={}", 
                event.getEventId(), event.getEventType(), exception.getMessage());
        // In production, this would persist the failed event to a database or queue
    }
}

