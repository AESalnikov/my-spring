package org.springframework.beans.factory;

/**
 * @author Anton Salnikov
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory);
}
