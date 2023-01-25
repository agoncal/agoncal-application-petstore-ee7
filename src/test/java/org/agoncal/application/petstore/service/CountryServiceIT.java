package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.Country;
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
public class CountryServiceIT
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private CountryService countryservice;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(AbstractService.class)
            .addClass(CountryService.class)
            .addClass(Country.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   // ======================================
   // =             Test Cases             =
   // ======================================

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(countryservice);
   }

   @Test
   public void should_crud()
   {
      // Gets all the objects
      int initialSize = countryservice.listAll().size();

      // Creates an object
      Country country = new Country("DV", "Dummy value", "Dummy value", "DMV", "DMV");

      // Inserts the object into the database
      country = countryservice.persist(country);
      assertNotNull(country.getId());
      assertEquals(initialSize + 1, countryservice.listAll().size());

      // Finds the object from the database and checks it's the right one
      country = countryservice.findById(country.getId());
      assertEquals("Dummy value", country.getName());

      // Updates the object
      country.setName("A new value");
      country = countryservice.merge(country);

      // Finds the object from the database and checks it has been updated
      country = countryservice.findById(country.getId());
      assertEquals("A new value", country.getName());

      // Deletes the object from the database and checks it's not there anymore
      countryservice.remove(country);
      assertEquals(initialSize, countryservice.listAll().size());
   }
}
