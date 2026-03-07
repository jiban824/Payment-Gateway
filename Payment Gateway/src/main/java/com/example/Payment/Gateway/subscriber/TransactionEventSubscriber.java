package com.example.Payment.Gateway.subscriber;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.Payment.Gateway.event.EventBus;
import com.example.Payment.Gateway.event.TransactionEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionEventSubscriber {
    private static final int MAX_MAILBOX_SIZE = 1000;
    private static final long[] RETRY_DELAYS_MS = {100, 500, 1000, 2000};
    
    private final BlockingQueue<TransactionEvent> mailbox = new LinkedBlockingQueue<>();
    private final AtomicInteger droppedCount = new AtomicInteger(0);
    private final DeadLetterQueue deadLetterQueue = new DeadLetterQueue();
    @SuppressWarnings("unused")
    private final EventBus eventBus;

    public TransactionEventSubscriber(EventBus eventBus) {
        this.eventBus = eventBus;
        // Register with event bus
        eventBus.subscribe(this::onEvent);

        // Start consumer thread
        Thread.ofVirtual().name("event-subscriber").start(this::processMailbox);
        log.info("[Subscriber] Transaction event subscriber initialized");
    }

    private void onEvent(TransactionEvent event) {
        if (mailbox.size() >= MAX_MAILBOX_SIZE) {
            if (event.isCritical()) {
                // Force add critical events by removing a non-critical one if possible
                log.warn("[Subscriber][BackPressure] Mailbox full. Forcing critical event in | eventId={}", event.getEventId());
                mailbox.removeIf(e -> !e.isCritical());
                mailbox.offer(event);
            } else {
                int dropped = droppedCount.incrementAndGet();
                log.warn("[Subscriber][BackPressure] Mailbox full. Dropping non-critical event | eventId={} totalDropped={}",
                        event.getEventId(), dropped);
            }
        } else {
            mailbox.offer(event);
        }
    }

    private void processMailbox() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                TransactionEvent event = mailbox.take();
                processWithRetry(event);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[Subscriber] Consumer thread interrupted");
            }
        }
    }

    private void processWithRetry(TransactionEvent event) {
        for (int attempt = 0; attempt <= RETRY_DELAYS_MS.length; attempt++) {
            try {
                simulateWebhook(event);
                return; // success
            } catch (Exception e) {
                if (attempt < RETRY_DELAYS_MS.length) {
                    long delay = RETRY_DELAYS_MS[attempt];
                    log.warn("[Subscriber][Retry] Attempt {}/{} failed for eventId={}. Retrying in {}ms | error={}",
                            attempt + 1, RETRY_DELAYS_MS.length + 1, event.getEventId(), delay, e.getMessage());
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                } else {
                    log.error("[Subscriber] All retries exhausted for eventId={}. Moving to DLQ.", event.getEventId());
                    deadLetterQueue.enqueue(event, e);
                }
            }
        }
    }

    /**
     * Simulates sending a webhook notification.
     * In production, this would make an HTTP POST to the merchant's webhook URL.
     */
    private void simulateWebhook(TransactionEvent event) {
        log.info("[Webhook] SIMULATED DELIVERY | eventType={} txnId={} merchantId={} amount={} currency={} timestamp={}",
                event.getEventType(),
                event.getTransactionId(),
                event.getMerchantId(),
                event.getAmount(),
                event.getCurrency(),
                event.getTimestamp());

        // Simulate occasional failures (10% chance) - remove in production
        if (Math.random() < 0.1) {
            throw new RuntimeException("Simulated webhook delivery failure");
        }
    }
}
