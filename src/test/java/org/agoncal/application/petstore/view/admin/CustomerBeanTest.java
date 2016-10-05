package org.agoncal.application.petstore.view.admin;

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

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CustomerBeanTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private CustomerBean customerbean;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(CustomerBean.class)
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
      Assert.assertNotNull(customerbean);
   }

   @Test
   public void should_crud()
   {
      // Creates an object
      Country country = new Country("DV", "Dummy value", "Dummy value", "DMV", "DMV");
      Address address = new Address("Dummy value", "Dummy value", "DV", country);
      Customer customer = new Customer("Dummy value", "Dummy value", "Dummy", "Dummy value", "Dummy value", address);

      // Inserts the object into the database
      customerbean.setCustomer(customer);
      customerbean.create();
      customerbean.update();
      customer = customerbean.getCustomer();
      assertNotNull(customer.getId());

      // Finds the object from the database and checks it's the right one
      customer = customerbean.findById(customer.getId());
      assertEquals("Dummy value", customer.getFirstName());


      // Deletes the object from the database and checks it's not there anymore
      customerbean.setId(customer.getId());
      customerbean.create();
      customerbean.delete();
      customer = customerbean.findById(customer.getId());
      assertNull(customer);
   }

   @Test
   public void should_paginate()
   {
      // Creates an empty example
      Customer example = new Customer();

      // Paginates through the example
      customerbean.setExample(example);
      customerbean.paginate();
      assertTrue((customerbean.getPageItems().size() == customerbean.getPageSize()) || (customerbean.getPageItems().size() == customerbean.getCount()));
   }
}
