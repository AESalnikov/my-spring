package org.springframework.context;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Anton Salnikov
 */
public class ApplicationContext {

    private BeanFactory beanFactory;

    public ApplicationContext(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Найти или создать бин
     *
     * @param type класс или интерфейс для создания бина
     * @param <T>
     * @return настроенный бин
     */
    public <T> T getBean(Class<T> type) {
        Class<? extends T> implClass = type;
        if (type.isInterface()) {
            implClass = beanFactory.findImplementation(type);
        }
        Map<String, Object> ioc = beanFactory.getSingletons();
        String beanName = implClass.getSimpleName().substring(0, 1).toLowerCase() + implClass.getSimpleName().substring(1);
        if (ioc.containsKey(beanName)) {
            return (T) ioc.get(beanName);
        }
        return beanFactory.getBean(implClass);
    }

    public ApplicationContext ready() {
        beanFactory.getApplicationListeners().forEach(applicationListener -> {
            for (Type type : applicationListener.getClass().getGenericInterfaces()) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type firstParameter = parameterizedType.getActualTypeArguments()[0];
                    if (firstParameter.equals(ApplicationReadyEvent.class)) {
                        applicationListener.onApplicationEvent(new ApplicationReadyEvent());
                    }
                }
            }
        });
        return this;
    }

    public void close() {
        beanFactory.close();
        beanFactory.getApplicationListeners().forEach(applicationListener -> {
            for (Type type : applicationListener.getClass().getGenericInterfaces()) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type firstParameter = parameterizedType.getActualTypeArguments()[0];
                    if (firstParameter.equals(ContextClosedEvent.class)) {
                        applicationListener.onApplicationEvent(new ContextClosedEvent());
                    }
                }
            }
        });
    }

}
