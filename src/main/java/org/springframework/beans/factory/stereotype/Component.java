package org.springframework.beans.factory.stereotype;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Anton Salnikov
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Component {
}
