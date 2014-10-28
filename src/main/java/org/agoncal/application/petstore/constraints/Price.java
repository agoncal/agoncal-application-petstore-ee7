package org.agoncal.application.petstore.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.DecimalMin;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@DecimalMin("10")
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Documented
public @interface Price {

    // ======================================
    // =             Attributes             =
    // ======================================

    String message() default "{org.agoncal.application.petstore.constraint.Price.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
