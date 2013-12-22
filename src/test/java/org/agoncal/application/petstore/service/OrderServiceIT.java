package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.*;
import org.agoncal.application.petstore.exception.ValidationException;
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
public class OrderServiceIT extends AbstractServiceIT {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private OrderService orderService;
    @Inject
    private CustomerService customerService;

    // ======================================
    // =              Unit tests            =
    // ======================================

    @Test
    @Ignore("TODO Not finished")
    public void shouldCRUDanOrder() {

        // Finds all the objects
        int initialNumber = orderService.findAllOrders().size();

        // Creates an object
        Address address = new Address("78 Gnu Rd", "Texas", "666", "WWW");
        Customer customer = new Customer("Richard", "Stallman", "rich", "rich", "rich@gnu.org", address);
        CreditCard creditCard = new CreditCard("1234", CreditCardType.MASTER_CARD, "10/12");
        List<CartItem> cartItems = new ArrayList<>();

        // Persists the object
        customer = customerService.createCustomer(customer);
        Order order = orderService.createOrder(customer, creditCard, cartItems);
        Long id = order.getId();

        // Finds all the objects and checks there's an extra one
        assertEquals("Should have an extra object", initialNumber + 1, orderService.findAllOrders().size());

        // Finds the object by id
        order = orderService.findOrder(id);
        assertNotNull(order.getOrderDate());

        // Deletes the object
        orderService.removeOrder(order);

        // Checks the object has been deleted
        assertNull("Should has been deleted", orderService.findOrder(id));

        // Finds all the objects and checks there's one less
        assertEquals("Should have an extra object", initialNumber, orderService.findAllOrders().size());
    }

    @Test(expected = ValidationException.class)
    public void shouldNotCreateAnOrderWithAnEmptyCart() {

        // Creates an object
        Address address = new Address("78 Gnu Rd", "Texas", "666", "WWW");
        Customer customer = new Customer("Richard", "Stallman", "rich", "rich", "rich@gnu.org", address);
        CreditCard creditCard = new CreditCard("1234", CreditCardType.MASTER_CARD, "10/12");
        List<CartItem> cartItems = new ArrayList<>();

        // Persists the object
        orderService.createOrder(customer, creditCard, cartItems);
    }
}
