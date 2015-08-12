package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.model.OrderLine;
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
public class OrderLineServiceTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private OrderLineService orderlineservice;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(AbstractService.class)
            .addClass(OrderLineService.class)
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
      Assert.assertNotNull(orderlineservice);
   }

   @Test
   public void should_crud()
   {
      // Gets all the objects
      int initialSize = orderlineservice.listAll().size();

      // Creates an object
      Category category = new Category("Dummy value", "Dummy value");
      Product product = new Product("Dummy value", "Dummy value", category);
      Item item = new Item("Dummy value", 10f, "Dummy value", "Dummy value", product);
      OrderLine orderLine = new OrderLine(77, item);

      // Inserts the object into the database
      orderLine = orderlineservice.persist(orderLine);
      assertNotNull(orderLine.getId());
      assertEquals(initialSize + 1, orderlineservice.listAll().size());

      // Finds the object from the database and checks it's the right one
      orderLine = orderlineservice.findById(orderLine.getId());
      assertEquals(new Integer(77), orderLine.getQuantity());

      // Updates the object
      orderLine.setQuantity(88);
      orderLine = orderlineservice.merge(orderLine);

      // Finds the object from the database and checks it has been updated
      orderLine = orderlineservice.findById(orderLine.getId());
      assertEquals(new Integer(88), orderLine.getQuantity());

      // Deletes the object from the database and checks it's not there anymore
      orderlineservice.remove(orderLine);
      assertEquals(initialSize, orderlineservice.listAll().size());
   }
}
