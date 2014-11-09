package org.agoncal.application.petstore.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.DecimalMin;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Constraint(validatedBy = {})
@DecimalMin("10")
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER,
      ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR})
@Documented
public @interface Price
{

   // ======================================
   // =             Attributes             =
   // ======================================

   String message() default "{org.agoncal.application.petstore.constraints.Price.message}";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};

   // ======================================
   // =          Inner Annotation          =
   // ======================================

   @Retention(RetentionPolicy.RUNTIME)
   @Target( {ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER,
         ElementType.TYPE, ElementType.ANNOTATION_TYPE,
         ElementType.CONSTRUCTOR})
   public @interface List
   {
      Price[] value();
   }
}
