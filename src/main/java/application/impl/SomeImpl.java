package application;

import org.springframework.beans.factory.stereotype.Component;

/**
 * @author Anton Salnikov
 */
@ProxySome
public class Some implements Mark {
    @Override
    public void doSome() {
        System.out.println("working...");
    }
}
