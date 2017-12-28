package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.util.Loggable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Stateless
@LocalBean
@Loggable
public class CustomerService extends AbstractService<Customer> implements Serializable
{

   // ======================================
   // =            Constructors            =
   // ======================================

   public CustomerService()
   {
      super(Customer.class);
   }

   // ======================================
   // =             Attributes             =
   // ======================================


   // ======================================
   // =              Public Methods        =
   // ======================================

   public boolean doesLoginAlreadyExist(@NotNull String login)
   {

      // Login has to be unique
      TypedQuery<Customer> typedQuery = entityManager.createNamedQuery(Customer.FIND_BY_LOGIN, Customer.class);
      typedQuery.setParameter("login", login);
      try
      {
         typedQuery.getSingleResult();
         return true;
      }
      catch (NoResultException e)
      {
         return false;
      }
   }

   public Customer createCustomer(@NotNull Customer customer)
   {
      Country country = entityManager.find(Country.class, customer.getHomeAddress().getCountry().getId());
      customer.getHomeAddress().setCountry(country);
      entityManager.persist(customer);
      return customer;
   }

   public Customer findCustomer(@NotNull String login)
   {
      TypedQuery<Customer> typedQuery = entityManager.createNamedQuery(Customer.FIND_BY_LOGIN, Customer.class);
      typedQuery.setParameter("login", login);

      try
      {
         return typedQuery.getSingleResult();
      }
      catch (NoResultException e)
      {
         return null;
      }
   }

   public Customer findCustomer(@NotNull String login, @NotNull String password)
   {
      TypedQuery<Customer> typedQuery = entityManager.createNamedQuery(Customer.FIND_BY_LOGIN_PASSWORD, Customer.class);
      typedQuery.setParameter("login", login);
      typedQuery.setParameter("password", password);

      return typedQuery.getSingleResult();
   }

   public List<Customer> findAllCustomers()
   {
      TypedQuery<Customer> typedQuery = entityManager.createNamedQuery(Customer.FIND_ALL, Customer.class);
      return typedQuery.getResultList();
   }

   public Customer updateCustomer(@NotNull Customer customer)
   {
      entityManager.merge(customer);
      return customer;
   }

   public void removeCustomer(@NotNull Customer customer)
   {
      entityManager.remove(entityManager.merge(customer));
   }

   public Country findCountry(@NotNull Long countryId)
   {
      return entityManager.find(Country.class, countryId);
   }

   // ======================================
   // =         Protected methods          =
   // ======================================

   @Override
   protected Predicate[] getSearchPredicates(Root<Customer> root, Customer example)
   {
      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String firstName = example.getFirstName();
      if (firstName != null && !"".equals(firstName))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("firstName")), '%' + firstName.toLowerCase() + '%'));
      }
      String lastName = example.getLastName();
      if (lastName != null && !"".equals(lastName))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("lastName")), '%' + lastName.toLowerCase() + '%'));
      }
      String telephone = example.getTelephone();
      if (telephone != null && !"".equals(telephone))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("telephone")), '%' + telephone.toLowerCase() + '%'));
      }
      String email = example.getEmail();
      if (email != null && !"".equals(email))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("email")), '%' + email.toLowerCase() + '%'));
      }
      String login = example.getLogin();
      if (login != null && !"".equals(login))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("login")), '%' + login.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }
}
