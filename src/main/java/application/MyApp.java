package application;

import application.ifc.Owner;
import application.impl.RoomOwner;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author Anton Salnikov
 */
public class Application {
    public static void main(String[] args) {
        // TODO: 28.06.2023 тут будет запускаться контекст
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.initiateSpringBeans();
        beanFactory.instantiate("application");
        beanFactory.initializeBeanPostProcessors();
        beanFactory.injectBeanFactory();
        beanFactory.initializeBeans();

//        Owner owner = beanFactory.getBean(Owner.class);
//        Room room = beanFactory.getBean(Room.class);
//        owner.putSomething();
//        room.getRoomObjects();

        Some s = beanFactory.getBean(Some.class);
        s.doSome();
    }
}
