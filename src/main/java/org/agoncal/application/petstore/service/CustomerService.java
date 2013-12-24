package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.util.Loggable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Stateless
@Loggable
public class CustomerService implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private EntityManager em;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public boolean doesLoginAlreadyExist(@NotNull String login) {

        // Login has to be unique
        TypedQuery<Customer> typedQuery = em.createNamedQuery(Customer.FIND_BY_LOGIN, Customer.class);
        typedQuery.setParameter("login", login);
        try {
            typedQuery.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public Customer createCustomer(@NotNull Customer customer) {
        em.persist(customer);
        return customer;
    }

    public Customer findCustomer(@NotNull String login) {
        TypedQuery<Customer> typedQuery = em.createNamedQuery(Customer.FIND_BY_LOGIN, Customer.class);
        typedQuery.setParameter("login", login);

        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Customer findCustomer(@NotNull String login, @NotNull String password) {
        TypedQuery<Customer> typedQuery = em.createNamedQuery(Customer.FIND_BY_LOGIN_PASSWORD, Customer.class);
        typedQuery.setParameter("login", login);
        typedQuery.setParameter("password", password);

        return typedQuery.getSingleResult();
    }

    public List<Customer> findAllCustomers() {
        TypedQuery<Customer> typedQuery = em.createNamedQuery(Customer.FIND_ALL, Customer.class);
        return typedQuery.getResultList();
    }

    public Customer updateCustomer(@NotNull Customer customer) {
        em.merge(customer);
        return customer;
    }

    public void removeCustomer(@NotNull Customer customer) {
        em.remove(em.merge(customer));
    }

    public Country findCountry(@NotNull Long countryId) {
        return em.find(Country.class, countryId);
    }

}
