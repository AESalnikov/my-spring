package org.springframework.context.event;

import org.springframework.context.ApplicationListener;

/**
 * @author Anton Salnikov
 */
public class ContextClosedListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("-------------------------");
        System.out.println("----------EVENT----------");
        System.out.println("------CONTEXT CLOSED-----");
        System.out.println("-------------------------");
    }
}
