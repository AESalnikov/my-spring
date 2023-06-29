package application.impl;

import application.ProxySome;
import application.Some;

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
