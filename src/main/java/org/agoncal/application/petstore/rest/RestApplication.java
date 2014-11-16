package org.agoncal.application.petstore.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@ApplicationPath("/rest")
public class RestApplication extends Application
{
   // ======================================
   // =          Business methods          =
   // ======================================

   // TODO Not sure this is still needed for portability in EE 7
//   @Override
//   public Set<Class<?>> getClasses()
//   {
//      Set<Class<?>> classes = new HashSet<>();
//      classes.add(CategoryEndpoint.class);
//      classes.add(CountryEndpoint.class);
//      classes.add(CustomerEndpoint.class);
//      classes.add(ItemEndpoint.class);
//      classes.add(ProductEndpoint.class);
//      return classes;
//   }
}