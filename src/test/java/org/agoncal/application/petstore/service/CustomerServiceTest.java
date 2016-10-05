package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.exceptions.ValidationException;
import org.agoncal.application.petstore.model.Address;
import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.model.UserRole;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class CustomerServiceTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private CustomerService customerservice;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(AbstractService.class)
            .addClass(CustomerService.class)
            .addClass(Customer.class)
            .addClass(Address.class)
            .addClass(Country.class)
            .addClass(UserRole.class)
            .addClass(ValidationException.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   // ======================================
   // =             Test Cases             =
   // ======================================

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(customerservice);
   }

   @Test
   public void should_crud()
   {
      // Gets all the objects
      int initialSize = customerservice.listAll().size();

      // Creates an object
      Country country = new Country("DV", "Dummy value", "Dummy value", "DMV", "DMV");
      Address address = new Address("Dummy value", "Dummy value", "DV", country);
      Customer customer = new Customer("Dummy value", "Dummy value", "Dummy", "Dummy value", "Dummy value", address);

      // Inserts the object into the database
      customer = customerservice.persist(customer);
      assertNotNull(customer.getId());
      assertEquals(initialSize + 1, customerservice.listAll().size());

      // Finds the object from the database and checks it's the right one
      customer = customerservice.findById(customer.getId());
      assertEquals("Dummy value", customer.getFirstName());

      // Updates the object
      customer.setFirstName("A new value");
      customer = customerservice.merge(customer);

      // Finds the object from the database and checks it has been updated
      customer = customerservice.findById(customer.getId());
      assertEquals("A new value", customer.getFirstName());

      // Deletes the object from the database and checks it's not there anymore
      customerservice.remove(customer);
      assertEquals(initialSize, customerservice.listAll().size());
   }
}
