package application.listener;

import org.springframework.beans.factory.stereotype.Component;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationReadyEvent;

/**
 * @author Anton Salnikov
 */
@Component
public class ContextReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("-----------------------");
        System.out.println("---------EVENT---------");
        System.out.println("-----CONTEXT READY-----");
        System.out.println("-----------------------");
    }

}
