package application;

import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Salnikov
 */
public class SomeBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Class<?>> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean.getClass().isAnnotationPresent(ProxySome.class)) {
            map.put(beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> beanClass = map.get(beanName);
        if (beanClass != null) {
            if (beanClass.getInterfaces().length != 0) {
                return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy, method, args) -> {
                    System.out.println("*****Proxy*****");
                    Object invoke = method.invoke(bean, args);
                    System.out.println("***************");
                    return invoke;
                });
            }
        }
        return bean;
    }
}
