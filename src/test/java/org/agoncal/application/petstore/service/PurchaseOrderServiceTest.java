package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.PurchaseOrder;
import org.agoncal.application.petstore.service.PurchaseOrderService;
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
import static org.hamcrest.core.Is.*;

@RunWith(Arquillian.class)
public class PurchaseOrderServiceTest
{

   @Inject
   private PurchaseOrderService purchaseorderservice;

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(AbstractService.class)
            .addClass(PurchaseOrderService.class)
            .addClass(PurchaseOrder.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(purchaseorderservice);
   }

   @Test
   public void should_crud()
   {
      // Gets all the objects
      int initialSize = purchaseorderservice.listAll().size();

      // Creates an object
      PurchaseOrder purchaseOrder = new PurchaseOrder();
      purchaseOrder.setDiscount(12.5F);

      // Inserts the object into the database
      purchaseOrder = purchaseorderservice.persist(purchaseOrder);
      assertNotNull(purchaseOrder.getId());
      assertEquals(initialSize + 1, purchaseorderservice.listAll().size());

      // Finds the object from the database and checks it's the right one
      purchaseOrder = purchaseorderservice.findById(purchaseOrder.getId());
      assertEquals(new Float(12.5F), purchaseOrder.getDiscountRate());

      // Updates the object
      purchaseOrder.setDiscount(43.25F);
      purchaseOrder = purchaseorderservice.merge(purchaseOrder);

      // Finds the object from the database and checks it has been updated
      purchaseOrder = purchaseorderservice.findById(purchaseOrder.getId());
      assertEquals(new Float(43.25F), purchaseOrder.getDiscountRate());

      // Deletes the object from the database and checks it's not there anymore
      purchaseorderservice.remove(purchaseOrder);
      assertEquals(initialSize, purchaseorderservice.listAll().size());
   }
}
