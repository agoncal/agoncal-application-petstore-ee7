package org.agoncal.application.petstore.view.admin;

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.model.OrderLine;
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
public class OrderLineBeanIT
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private OrderLineBean orderlinebean;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(OrderLineBean.class)
            .addClass(OrderLine.class)
            .addClass(Category.class)
            .addClass(Product.class)
            .addClass(Item.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   // ======================================
   // =             Test Cases             =
   // ======================================

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(orderlinebean);
   }

   @Test
   public void should_crud()
   {
      // Creates an object
      Category category = new Category("Dummy value", "Dummy value");
      Product product = new Product("Dummy value", "Dummy value", category);
      Item item = new Item("Dummy value", 10f, "Dummy value", "Dummy value", product);
      OrderLine orderLine = new OrderLine(77, item);

      // Inserts the object into the database
      orderlinebean.setOrderLine(orderLine);
      orderlinebean.create();
      orderlinebean.update();
      orderLine = orderlinebean.getOrderLine();
      assertNotNull(orderLine.getId());

      // Finds the object from the database and checks it's the right one
      orderLine = orderlinebean.findById(orderLine.getId());
      assertEquals(new Integer(77), orderLine.getQuantity());

      // Deletes the object from the database and checks it's not there anymore
      orderlinebean.setId(orderLine.getId());
      orderlinebean.create();
      orderlinebean.delete();
      orderLine = orderlinebean.findById(orderLine.getId());
      assertNull(orderLine);
   }

   @Test
   public void should_paginate()
   {
      // Creates an empty example
      OrderLine example = new OrderLine();

      // Paginates through the example
      orderlinebean.setExample(example);
      orderlinebean.paginate();
      assertTrue((orderlinebean.getPageItems().size() == orderlinebean.getPageSize()) || (orderlinebean.getPageItems().size() == orderlinebean.getCount()));
   }
}
