package application.listener;

import org.springframework.beans.factory.stereotype.Component;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author Anton Salnikov
 */
@Component
public class ContextClosedListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("-------------------------");
        System.out.println("----------EVENT----------");
        System.out.println("------CONTEXT CLOSED-----");
        System.out.println("-------------------------");
    }
}
