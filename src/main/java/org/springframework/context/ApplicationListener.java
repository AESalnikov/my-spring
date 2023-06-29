package org.springframework.context.event;

/**
 * @author Anton Salnikov
 */
public interface ApplicationListener <E> {
    void onApplicationEvent(E event);
}
