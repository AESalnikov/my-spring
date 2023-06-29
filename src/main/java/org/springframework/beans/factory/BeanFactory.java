package org.springframework.beans.factory;

import org.springframework.beans.factory.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.PreDestroy;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.stereotype.Component;
import org.springframework.context.ApplicationListener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.springframework.util.Constant.SPRING_BASE_PACKAGE;

/**
 * @author Anton Salnikov
 */
public class BeanFactory {

    private Map<String, Object> singletons = new ConcurrentHashMap<>();
    private List<ApplicationListener> applicationListeners = new ArrayList<>();
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    private final String BASE_PACKAGE;

    public BeanFactory(String basePackage) {
        this.BASE_PACKAGE = basePackage;
    }

    /**
     * Ищем, создаем и запоминаем бины
     */
    public void instantiate() {
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] instantiate...");
        Consumer<Class<?>> initiate = aClass -> {
            if (aClass.isAnnotationPresent(Component.class)) {
                try {
                    Object instance = aClass.getDeclaredConstructor().newInstance();
                    String beanName = aClass.getSimpleName().substring(0, 1).toLowerCase() + aClass.getSimpleName().substring(1);
                    singletons.put(beanName, instance);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        initiateAfterScan(BASE_PACKAGE, initiate);
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] initiated");
    }

    /**
     * Ищем классы в пакете и его подпакетах
     *
     * @param dir         директория
     * @param basePackage имя пакета
     * @return список найденных классов
     */
    private List<Class<?>> findClasses(File dir, String basePackage) {
        List<Class<?>> classes = new ArrayList<>();
        if (!dir.exists()) {
            return classes;
        }
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, basePackage + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().lastIndexOf("."));
                try {
                    classes.add(Class.forName(basePackage + "." + className));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return classes;
    }

    /**
     * Ищем, создаем и applicationLister'ы
     */
    public void initializeApplicationListeners() {
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] application listeners initialize...");
        Consumer<Class<?>> initiate = listenerClass -> {
            if (List.of(listenerClass.getInterfaces()).contains(ApplicationListener.class)) {
                try {
                    applicationListeners.add((ApplicationListener) listenerClass.getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        initiateAfterScan(SPRING_BASE_PACKAGE, initiate);
        initiateAfterScan(BASE_PACKAGE, initiate);
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] application listeners initialized");
    }

    /**
     * Ищем, создаем и beanPostProcessor'ы
     */
    public void initializeBeanPostProcessors() throws RuntimeException {
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] beanPostProcessors initialize...");
        Consumer<Class<?>> initiate = aClass -> {
            if (List.of(aClass.getInterfaces()).contains(BeanPostProcessor.class)) {
                try {
                    if (List.of(aClass.getInterfaces()).contains(BeanPostProcessor.class)) {
                        beanPostProcessors.add((BeanPostProcessor) aClass.getDeclaredConstructor().newInstance());
                    }
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        initiateAfterScan(SPRING_BASE_PACKAGE, initiate);
        initiateAfterScan(BASE_PACKAGE, initiate);
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] beanPostProcessors initialized");
    }

    /**
     * Инициализируем бины
     */
    public void initializeBeans() {
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] initialize beans...");
        singletons.forEach((beanName, bean) -> {
            beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessBeforeInitialization(bean, beanName));
            invokeInitMethod(bean.getClass(), bean);
            beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessAfterInitialization(bean, beanName));
        });
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] beans initialized");
    }

    /**
     * Вызываем init-метод
     *
     * @param implClass оригинальный класс у которого нужно вызвать метод
     * @param bean      объект у которого нужно вызвать метод
     * @param <T>
     */
    private <T> void invokeInitMethod(Class<? extends T> implClass, T bean) {
        for (Method method : implClass.getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                try {
                    method.invoke(bean);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Создаем бин из класса
     *
     * @param implClass класс из которого нужно создать бин
     * @param <T>
     * @return настроенный бин
     */
    public <T> T getBean(Class<T> implClass) {
        try {
            T bean = implClass.getDeclaredConstructor().newInstance();
            String beanName = implClass.getSimpleName().substring(0, 1).toLowerCase() + implClass.getSimpleName().substring(1);
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                bean = (T) beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            }
            invokeInitMethod(implClass, bean);
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                bean = (T) beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            }
            return bean;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Инжектим beanFactory в бины реализующие интерфейс BeanFactoryAware
     */
    public void injectBeanFactory() {
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] bean factory inject...");
        singletons.forEach((beanName, bean) -> {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
        });
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            if (beanPostProcessor instanceof BeanFactoryAware) {
                ((BeanFactoryAware) beanPostProcessor).setBeanFactory(this);
            }
        }
        System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "] bean factory injected");
    }

    /**
     * Ищем имплементацию интерфейса
     *
     * @param type интерфейс
     * @param <T>
     * @return класс имплементирующий интерфейс type
     */
    public <T> Class<? extends T> findImplementation(Class<T> type) {
        Set<Object> beans = new HashSet<>();
        singletons.forEach((beanName, bean) -> {
            if (List.of(bean.getClass().getInterfaces()).contains(type)) {
                beans.add(bean);
            }
        });
        if (beans.isEmpty()) {
            List<Class<?>> implClasses = new ArrayList<>();
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            String path = BASE_PACKAGE.replace(".", "/");
            try {
                Enumeration<URL> resources = classLoader.getResources(path);
                while (resources.hasMoreElements()) {
                    URL resource = resources.nextElement();
                    File file = new File(resource.toURI());
                    List<Class<?>> classes = findClasses(file, BASE_PACKAGE);
                    for (Class<?> aClass : classes) {
                        if (List.of(aClass.getInterfaces()).contains(type)) {
                            implClasses.add(aClass);
                        }
                    }
                }
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
            if (implClasses.size() != 1) {
                throw new RuntimeException("Найдено более одной реализации интерфейса " + type);
            }
            return (Class<? extends T>) implClasses.get(0);
        } else {
            if (beans.size() != 1) {
                throw new RuntimeException("Найдено более одной реализации интерфейса " + type);
            }
            return (Class<? extends T>) beans.iterator().next().getClass();
        }
    }

    /**
     * Проходимя по всем просканированным классам и запускаем определенную логику для каждого класса
     *
     * @param basePackage пакет
     * @param initiate    логика которую нужно запустить
     */
    private void initiateAfterScan(String basePackage, Consumer<Class<?>> initiate) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String path = basePackage.replace(".", "/");
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.toURI());
                List<Class<?>> classes = findClasses(file, basePackage);
                for (Class<?> aClass : classes) {
                    initiate.accept(aClass);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вызываем все методы помеченные @PreDestroy
     */
    public void close() {
        singletons.forEach((beanName, bean) -> {
            for (Method method : bean.getClass().getMethods()) {
                if (method.isAnnotationPresent(PreDestroy.class)) {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public Map<String, Object> getSingletons() {
        return singletons;
    }

    public List<ApplicationListener> getApplicationListeners() {
        return applicationListeners;
    }

}
