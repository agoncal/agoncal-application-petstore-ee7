package org.agoncal.application.petstore.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.agoncal.application.petstore.constraints.Email;
import org.agoncal.application.petstore.constraints.Login;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Entity
@NamedQueries({
         @NamedQuery(name = Customer.FIND_BY_LOGIN, query = "SELECT c FROM Customer c WHERE c.login = :login"),
         @NamedQuery(name = Customer.FIND_BY_EMAIL, query = "SELECT c FROM Customer c WHERE c.email = :email"),
         @NamedQuery(name = Customer.FIND_BY_LOGIN_PASSWORD, query = "SELECT c FROM Customer c WHERE c.login = :login AND c.password = :password"),
         @NamedQuery(name = Customer.FIND_BY_UUID, query = "SELECT c FROM Customer c WHERE c.uuid = :uuid"),
         @NamedQuery(name = Customer.FIND_ALL, query = "SELECT c FROM Customer c")
})
@XmlRootElement
public class Customer implements Serializable
{

   // ======================================
   // = Attributes =
   // ======================================

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   @Version
   @Column(name = "version")
   private int version;

   @Column(length = 50, name = "first_name", nullable = false)
   @NotNull
   @Size(min = 2, max = 50)
   private String firstName;

   @Column(length = 50, name = "last_name", nullable = false)
   @NotNull
   @Size(min = 2, max = 50)
   private String lastName;

   @Column
   private String telephone;

   @Column
   @Email
   private String email;

   @Column(length = 10, nullable = false)
   @Login
   private String login;

   @Column(length = 256, nullable = false)
   @NotNull
   @Size(min = 1, max = 256)
   private String password;

   @Column(length = 256)
   @Size(min = 1, max = 256)
   private String uuid;

   private UserRole role;

   @Column(name = "date_of_birth")
   @Temporal(TemporalType.DATE)
   @Past
   private Date dateOfBirth;

   @Transient
   private Integer age;

   @Embedded
   @Valid
   private Address homeAddress = new Address();

   // ======================================
   // = Constants =
   // ======================================

   public static final String FIND_BY_LOGIN = "Customer.findByLogin";
   public static final String FIND_BY_LOGIN_PASSWORD = "Customer.findByLoginAndPassword";
   public static final String FIND_ALL = "Customer.findAll";
   public static final String FIND_BY_EMAIL = "Customer.findByEmail";
   public static final String FIND_BY_UUID = "Customer.findByUUID";

   // ======================================
   // = Constructors =
   // ======================================

   public Customer()
   {
   }

   public Customer(String firstName, String lastName, String login, String plainTextPassword, String email,
            Address address)
   {
      this.firstName = firstName;
      this.lastName = lastName;
      this.login = login;
      this.password = digestPassword(plainTextPassword);
      this.email = email;
      this.homeAddress = address;
      this.dateOfBirth = new Date();
   }

   // ======================================
   // = Lifecycle Methods =
   // ======================================

   /**
    * This method calculates the age of the customer
    */
   @PostLoad
   @PostPersist
   @PostUpdate
   public void calculateAge()
   {
      if (dateOfBirth == null)
      {
         age = null;
         return;
      }

      Calendar birth = new GregorianCalendar();
      birth.setTime(dateOfBirth);
      Calendar now = new GregorianCalendar();
      now.setTime(new Date());
      int adjust = 0;
      if (now.get(Calendar.DAY_OF_YEAR) - birth.get(Calendar.DAY_OF_YEAR) < 0)
      {
         adjust = -1;
      }
      age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + adjust;
   }

   @PrePersist
   private void digestPassword()
   {
      password = digestPassword(password);
   }

   // ======================================
   // = Business methods =
   // ======================================

   /**
    * Digest password with <code>SHA-256</code> then encode it with Base64.
    *
    * @param plainTextPassword the password to digest and encode
    * @return digested password
    */
   public String digestPassword(String plainTextPassword)
   {
      try
      {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         md.update(plainTextPassword.getBytes("UTF-8"));
         byte[] passwordDigest = md.digest();
         return Base64.getEncoder().encodeToString(passwordDigest);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Exception encoding password", e);
      }
   }

   // ======================================
   // = Getters & setters =
   // ======================================

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   public String getLogin()
   {
      return login;
   }

   public void setLogin(String login)
   {
      this.login = login;
   }

   public UserRole getRole()
   {
      return role;
   }

   public void setRole(UserRole role)
   {
      this.role = role;
   }

   public String getUuid()
   {
      return uuid;
   }

   public void setUuid(String uuid)
   {
      this.uuid = uuid;
   }

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   public String getFirstName()
   {
      return firstName;
   }

   public void setFirstName(String firstName)
   {
      this.firstName = firstName;
   }

   public String getLastName()
   {
      return lastName;
   }

   public void setLastName(String lastName)
   {
      this.lastName = lastName;
   }

   public String getFullName()
   {
      return firstName + " " + lastName;
   }

   public String getTelephone()
   {
      return telephone;
   }

   public void setTelephone(String telephone)
   {
      this.telephone = telephone;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public Date getDateOfBirth()
   {
      return dateOfBirth;
   }

   public void setDateOfBirth(Date dateOfBirth)
   {
      this.dateOfBirth = dateOfBirth;
   }

   public Integer getAge()
   {
      return age;
   }

   public Address getHomeAddress()
   {
      return homeAddress;
   }

   public void setHomeAddress(Address homeAddress)
   {
      this.homeAddress = homeAddress;
   }

   // ======================================
   // = Methods hash, equals, toString =
   // ======================================

   @Override
   public final boolean equals(Object o)
   {
      if (this == o)
         return true;
      if (!(o instanceof Customer))
         return false;
      Customer customer = (Customer) o;
      return Objects.equals(login, customer.login);
   }

   @Override
   public final int hashCode()
   {
      return Objects.hash(login);
   }

   @Override
   public String toString()
   {
      return firstName + ' ' + lastName + " (" + login + ")";
   }
}
