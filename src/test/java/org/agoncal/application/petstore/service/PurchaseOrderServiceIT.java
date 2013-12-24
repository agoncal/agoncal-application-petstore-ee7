package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.exception.ValidationException;
import org.agoncal.application.petstore.model.*;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 */
@RunWith(Arquillian.class)
public class PurchaseOrderServiceIT extends AbstractServiceIT {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private PurchaseOrderService purchaseOrderService;
    @Inject
    private CustomerService customerService;

    // ======================================
    // =              Unit tests            =
    // ======================================

    @Test
    @Ignore("TODO Not finished")
    public void shouldCRUDanOrder() {

        // Finds all the objects
        int initialNumber = purchaseOrderService.findAllOrders().size();

        // Creates an object
        Country country = new Country("33", "France", "France");
        Address address = new Address("78 Gnu Rd", "Texas", "666", country);
        Customer customer = new Customer("Richard", "Stallman", "rich", "rich", "rich@gnu.org", address);
        CreditCard creditCard = new CreditCard("1234", CreditCardType.MASTER_CARD, "10/12");
        List<ShoppingCartItem> cartItems = new ArrayList<>();

        // Persists the object
        customer = customerService.createCustomer(customer);
        PurchaseOrder order = purchaseOrderService.createOrder(customer, creditCard, cartItems);
        Long id = order.getId();

        // Finds all the objects and checks there's an extra one
        assertEquals("Should have an extra object", initialNumber + 1, purchaseOrderService.findAllOrders().size());

        // Finds the object by id
        order = purchaseOrderService.findOrder(id);
        assertNotNull(order.getOrderDate());

        // Deletes the object
        purchaseOrderService.removeOrder(order);

        // Checks the object has been deleted
        assertNull("Should has been deleted", purchaseOrderService.findOrder(id));

        // Finds all the objects and checks there's one less
        assertEquals("Should have an extra object", initialNumber, purchaseOrderService.findAllOrders().size());
    }

    @Test(expected = ValidationException.class)
    public void shouldNotCreateAnOrderWithAnEmptyCart() {

        // Creates an object
        Country country = new Country("33", "France", "France");
        Address address = new Address("78 Gnu Rd", "Texas", "666", country);
        Customer customer = new Customer("Richard", "Stallman", "rich", "rich", "rich@gnu.org", address);
        CreditCard creditCard = new CreditCard("1234", CreditCardType.MASTER_CARD, "10/12");
        List<ShoppingCartItem> cartItems = new ArrayList<>();

        // Persists the object
        purchaseOrderService.createOrder(customer, creditCard, cartItems);
    }
}
