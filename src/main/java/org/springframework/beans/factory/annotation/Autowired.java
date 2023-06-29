package org.springframework.beans.factory.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Anton Salnikov
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Autowired {
}
