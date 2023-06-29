package org.springframework.context;

/**
 * @author Anton Salnikov
 */
public interface ApplicationListener<E> {
    void onApplicationEvent(E event);
}
