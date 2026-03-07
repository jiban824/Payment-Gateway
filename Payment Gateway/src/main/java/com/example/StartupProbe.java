package com.example;

import java.util.Map;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartupProbe implements ApplicationListener<org.springframework.context.ApplicationEvent> {

    @Override
    public void onApplicationEvent(@NonNull org.springframework.context.ApplicationEvent event) {
        if (event instanceof ApplicationStartedEvent) {
            log.info("[Probe] ApplicationStartedEvent");
        } else if (event instanceof ApplicationReadyEvent) {
            log.info("[Probe] ApplicationReadyEvent — application is ready");
        } else if (event instanceof ApplicationFailedEvent afe) {
            log.error("[Probe] ApplicationFailedEvent — failure during startup", afe.getException());
        } else if (event instanceof ContextClosedEvent cce) {
            log.warn("[Probe] ContextClosedEvent — application context is closing. Dumping thread stacks and context info...");
            try {
                if (cce.getApplicationContext() != null) {
                    int beanCount = cce.getApplicationContext().getBeanDefinitionCount();
                    log.warn("[Probe] Bean definition count at close: {}", beanCount);
                }
            } catch (Exception e) {
                log.warn("[Probe] Failed reading context info", e);
            }

            try {
                Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
                for (Map.Entry<Thread, StackTraceElement[]> en : traces.entrySet()) {
                    Thread t = en.getKey();
                    StackTraceElement[] st = en.getValue();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Thread: ").append(t.getName()).append(" (state=").append(t.getState()).append(")\n");
                    for (int i = 0; i < Math.min(10, st.length); i++) {
                        sb.append("    at ").append(st[i].toString()).append("\n");
                    }
                    log.warn(sb.toString());
                }
            } catch (Exception e) {
                log.error("[Probe] Failed dumping thread stacks", e);
            }
        }
    }
}
