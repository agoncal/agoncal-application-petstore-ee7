package org.agoncal.application.petstore.view.admin;

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.model.Product;
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
public class ProductBeanIT
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private ProductBean productbean;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(ProductBean.class)
            .addClass(Product.class)
            .addClass(Category.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   // ======================================
   // =             Test Cases             =
   // ======================================

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(productbean);
   }

   @Test
   public void should_crud()
   {
      // Creates an object
      Category category = new Category("Dummy value", "Dummy value");
      Product product = new Product("Dummy value", "Dummy value", category);

      // Inserts the object into the database
      productbean.setProduct(product);
      productbean.create();
      productbean.update();
      product = productbean.getProduct();
      assertNotNull(product.getId());

      // Finds the object from the database and checks it's the right one
      product = productbean.findById(product.getId());
      assertEquals("Dummy value", product.getName());

      // Deletes the object from the database and checks it's not there anymore
      productbean.setId(product.getId());
      productbean.create();
      productbean.delete();
      product = productbean.findById(product.getId());
      assertNull(product);
   }

   @Test
   public void should_paginate()
   {
      // Creates an empty example
      Product example = new Product();

      // Paginates through the example
      productbean.setExample(example);
      productbean.paginate();
      assertTrue((productbean.getPageItems().size() == productbean.getPageSize()) || (productbean.getPageItems().size() == productbean.getCount()));
   }
}
