package org.springframework.context;

import org.springframework.beans.factory.BeanFactory;

import static org.springframework.util.Constant.BANNER;

/**
 * @author Anton Salnikov
 */
public class SpringApplication {

    public static ApplicationContext run(String basePackage) {
        System.out.println(BANNER);
        BeanFactory beanFactory = new BeanFactory(basePackage);
        beanFactory.initializeApplicationListeners();
        beanFactory.initializeBeanPostProcessors();
        beanFactory.instantiate();
        beanFactory.injectBeanFactory();
        beanFactory.initializeBeans();
        return new ApplicationContext(beanFactory).ready();
    }

}
