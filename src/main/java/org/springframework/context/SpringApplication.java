package org.springframework;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author Anton Salnikov
 */
public class SpringApplication {

    public static ApplicationContext run(String basePackage) {
        BeanFactory beanFactory = new BeanFactory(basePackage);
        beanFactory.initializeBeanPostProcessors();
        beanFactory.instantiate();
        beanFactory.injectBeanFactory();
        beanFactory.initializeBeans();
        return new ApplicationContext(beanFactory);
    }

}
