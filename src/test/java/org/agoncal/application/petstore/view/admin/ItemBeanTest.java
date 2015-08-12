package org.agoncal.application.petstore.view.admin;

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.model.Product;
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
public class ItemBeanTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private ItemBean itembean;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(ItemBean.class)
            .addClass(Item.class)
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
      Assert.assertNotNull(itembean);
   }

   @Test
   public void should_crud()
   {
      // Creates an object
      Category category = new Category("Dummy value", "Dummy value");
      Product product = new Product("Dummy value", "Dummy value", category);
      Item item = new Item("Dummy value", 10f, "Dummy value", "Dummy value", product);

      // Inserts the object into the database
      itembean.setItem(item);
      itembean.create();
      itembean.update();
      item = itembean.getItem();
      assertNotNull(item.getId());

      // Finds the object from the database and checks it's the right one
      item = itembean.findById(item.getId());
      assertEquals("Dummy value", item.getName());

      // Deletes the object from the database and checks it's not there anymore
      itembean.setId(item.getId());
      itembean.create();
      itembean.delete();
      item = itembean.findById(item.getId());
      assertNull(item);
   }

   @Test
   public void should_paginate()
   {
      // Creates an empty example
      Item example = new Item();

      // Paginates through the example
      itembean.setExample(example);
      itembean.paginate();
      assertTrue((itembean.getPageItems().size() == itembean.getPageSize()) || (itembean.getPageItems().size() == itembean.getCount()));
   }
}
