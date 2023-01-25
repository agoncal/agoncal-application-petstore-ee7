package org.agoncal.application.petstore.view.admin;

import org.agoncal.application.petstore.model.Country;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CountryBeanIT
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private CountryBean countrybean;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(CountryBean.class)
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
      Assert.assertNotNull(countrybean);
   }

   @Test
   public void should_crud()
   {
      // Creates an object
      Country country = new Country("DV", "Dummy value", "Dummy value", "DMV", "DMV");

      // Inserts the object into the database
      countrybean.setCountry(country);
      countrybean.create();
      countrybean.update();
      country = countrybean.getCountry();
      assertNotNull(country.getId());

      // Finds the object from the database and checks it's the right one
      country = countrybean.findById(country.getId());
      assertEquals("Dummy value", country.getName());

      // Deletes the object from the database and checks it's not there anymore
      countrybean.setId(country.getId());
      countrybean.create();
      countrybean.delete();
      country = countrybean.findById(country.getId());
      assertNull(country);
   }

   @Test
   public void should_paginate()
   {
      // Creates an empty example
      Country example = new Country();

      // Paginates through the example
      countrybean.setExample(example);
      countrybean.paginate();
      assertTrue((countrybean.getPageItems().size() == countrybean.getPageSize()) || (countrybean.getPageItems().size() == countrybean.getCount()));
   }
}
