package org.springframework.context.event;

import org.springframework.context.ApplicationListener;

/**
 * @author Anton Salnikov
 */
public class ContextReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("-----------------------");
        System.out.println("---------EVENT---------");
        System.out.println("-----CONTEXT READY-----");
        System.out.println("-----------------------");
    }

}
