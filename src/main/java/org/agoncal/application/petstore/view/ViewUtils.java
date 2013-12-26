package org.agoncal.application.petstore.view;

import javax.persistence.Id;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for working with Java Server Faces views.
 */

public final class ViewUtils
{

   public static <T> List<T> asList(Collection<T> collection)
   {

      if (collection == null)
      {
         return null;
      }

      return new ArrayList<T>(collection);
   }

   public static String display(Object object)
   {

      if (object == null)
      {
         return null;
      }

      try
      {
         // Invoke toString if declared in the class. If not found, the NoSuchMethodException is caught and handled
         object.getClass().getDeclaredMethod("toString");
         return object.toString();
      }
      catch (NoSuchMethodException noMethodEx)
      {
         try
         {
            for (Field field : object.getClass().getDeclaredFields())
            {
               // Find the primary key field and display it
               if (field.getAnnotation(Id.class) != null)
               {
                  // Find a matching getter and invoke it to display the key
                  for (Method method : object.getClass().getDeclaredMethods())
                  {
                     if (method.equals(new PropertyDescriptor(field.getName(), object.getClass()).getReadMethod()))
                     {
                        return method.invoke(object).toString();
                     }
                  }
               }
            }
            for (Method method : object.getClass().getDeclaredMethods())
            {
               // Find the primary key as a property instead of a field, and display it
               if (method.getAnnotation(Id.class) != null)
               {
                  return method.invoke(object).toString();
               }
            }
         }
         catch (Exception ex)
         {
            // Unlikely, but abort and stop view generation if any exception is thrown
            throw new RuntimeException(ex);
         }
      }

      return null;
   }

   private ViewUtils()
   {

      // Can never be called
   }
}
