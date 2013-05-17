package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.domain.Address;
import org.agoncal.application.petstore.domain.Customer;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 */
@RunWith(Arquillian.class)
public class CustomerServiceIT extends AbstractServiceIT {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private CustomerService customerService;

    // ======================================
    // =              Unit tests            =
    // ======================================

    @Test
    public void shouldCRUDaCustomer() {

        // Finds all the objects
        int initialNumber = customerService.findAllCustomers().size();

        // Creates an object
        Customer customer = new Customer("Richard", "Stallman", "rich", "rich", "rich@gnu.org", new Address("78 Gnu Rd", "Texas", "666", "WWW"));

        // Persists the object
        customer = customerService.createCustomer(customer);
        String login = customer.getLogin();

        // Finds all the objects and checks there's an extra one
        assertEquals("Should have an extra object", initialNumber + 1, customerService.findAllCustomers().size());

        // Finds the object by login
        customer = customerService.findCustomer(login);
        assertEquals("Richard", customer.getFirstname());

        // Updates the object
        customer.setFirstname("Rich");
        customerService.updateCustomer(customer);

        // Finds the object by login
        customer = customerService.findCustomer(login);
        assertEquals("Rich", customer.getFirstname());

        // Deletes the object
        customerService.removeCustomer(customer);

        // Checks the object has been deleted
        assertNull("Should has been deleted", customerService.findCustomer(login));

        // Finds all the objects and checks there's one less
        assertEquals("Should have an extra object", initialNumber, customerService.findAllCustomers().size());
    }
}
