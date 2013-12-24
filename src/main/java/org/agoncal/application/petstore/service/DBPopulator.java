package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.Address;
import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.util.Loggable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Singleton
@Startup
@Loggable
public class DBPopulator {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Customer marc;
    private Customer bill;
    private Customer steve;
    private Customer user;
    private Customer admin;

    @Inject
    private CustomerService customerService;

    // ======================================
    // =          Lifecycle Methods         =
    // ======================================

    @PostConstruct
    private void populateDB() {
        Country usa = customerService.findCountry(1225L);
        marc = new Customer("Marc", "Fleury", "marc", "marc", "marc@jboss.org", new Address("65 Ritherdon Road", "Los Angeles", "56421", usa));
        bill = new Customer("Bill", "Gates", "bill", "bill", "bill.gates@microsoft.com", new Address("27 West Side", "Alhabama", "8401", usa));
        steve = new Customer("Steve", "Jobs", "jobs", "jobs", "steve.jobs@apple.com", new Address("154 Star Boulevard", "San Francisco", "5455", usa));
        user = new Customer("User", "User", "user", "user", "user@petstore.org", new Address("Petstore", "Land", "666", usa));
        admin = new Customer("Admin", "Admin", "admin", "admin", "admin@petstore.org", new Address("Petstore", "Land", "666", usa));

        customerService.createCustomer(marc);
        customerService.createCustomer(bill);
        customerService.createCustomer(steve);
        customerService.createCustomer(user);
        customerService.createCustomer(admin);
    }

    @PreDestroy
    private void clearDB() {
        customerService.removeCustomer(marc);
        customerService.removeCustomer(bill);
        customerService.removeCustomer(steve);
        customerService.removeCustomer(user);
        customerService.removeCustomer(admin);
    }
}
