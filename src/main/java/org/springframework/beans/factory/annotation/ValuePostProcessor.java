package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.PropertiesFileInitializer;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Anton Salnikov
 */
public class ValuePostProcessor implements BeanPostProcessor {

    private Map<String, String> properties = new PropertiesFileInitializer().initialize("application.properties");

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            Value annotation = field.getAnnotation(Value.class);
            if (annotation != null) {
                String value = annotation.value().isEmpty()
                        ? properties.get(beanName + "." + field.getName())
                        : properties.get(beanName + "." + annotation.value());
                field.setAccessible(true);
                try {
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
