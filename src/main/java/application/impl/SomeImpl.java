package application.impl;

import application.Some;
import application.ProxySome;

/**
 * @author Anton Salnikov
 */
@ProxySome
public class SomeImpl implements Some {
    @Override
    public void doSome() {
        System.out.println("working...");
    }
}
