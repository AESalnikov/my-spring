package org.springframework.beans.factory.config;

/**
 * @author Anton Salnikov
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object bean, String beanName);
}
