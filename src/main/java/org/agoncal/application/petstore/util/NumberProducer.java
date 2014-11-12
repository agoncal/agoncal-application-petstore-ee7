package org.agoncal.application.petstore.util;

import javax.enterprise.inject.Produces;
import org.agoncal.application.petstore.util.Vat;
import javax.inject.Named;
import org.agoncal.application.petstore.util.Discount;

public class NumberProducer
{

   @Produces
   @Vat
   @Named
   private Float vatRate;
   @Produces
   @Discount
   @Named
   private Float discountRate;
}