package org.agoncal.application.petstore.util;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author blep
 *         --
 */

@Qualifier
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ConfigProperty {
    @Nonbinding String value() default "";
}
