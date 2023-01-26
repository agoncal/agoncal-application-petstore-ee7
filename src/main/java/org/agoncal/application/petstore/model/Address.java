package org.agoncal.application.petstore.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Embeddable
public class Address implements Serializable
{

   // ======================================
   // = Attributes =
   // ======================================

   @Column(length = 50, nullable = false)
   @Size(min = 5, max = 50)
   @NotNull
   private String street1;

   @Column
   private String street2;

   @Column(length = 50, nullable = false)
   @Size(min = 2, max = 50)
   @NotNull
   private String city;

   @Column
   private String state;

   @Column(length = 10, name = "zip_code", nullable = false)
   @Size(min = 1, max = 10)
   @NotNull
   private String zipcode;

   @ManyToOne
   private Country country = new Country();

   // ======================================
   // = Constructors =
   // ======================================

   public Address()
   {
   }

   public Address(String street1, String city, String zipcode, Country country)
   {
      this.street1 = street1;
      this.city = city;
      this.zipcode = zipcode;
      this.country = country;
   }

   // ======================================
   // = Getters & setters =
   // ======================================

   public String getStreet1()
   {
      return street1;
   }

   public void setStreet1(String street1)
   {
      this.street1 = street1;
   }

   public String getStreet2()
   {
      return street2;
   }

   public void setStreet2(String street2)
   {
      this.street2 = street2;
   }

   public String getCity()
   {
      return city;
   }

   public void setCity(String city)
   {
      this.city = city;
   }

   public String getState()
   {
      return state;
   }

   public void setState(String state)
   {
      this.state = state;
   }

   public String getZipcode()
   {
      return zipcode;
   }

   public void setZipcode(String zipcode)
   {
      this.zipcode = zipcode;
   }

   public Country getCountry()
   {
      return this.country;
   }

   public void setCountry(final Country country)
   {
      this.country = country;
   }

   // ======================================
   // = Methods hash, equals, toString =
   // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return street1.equals(address.street1) && city.equals(address.city) && zipcode.equals(address.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street1, city, zipcode);
    }

    @Override
   public String toString()
   {
      return "Address{" +
               "street1='" + street1 + '\'' +
               ", street2='" + street2 + '\'' +
               ", city='" + city + '\'' +
               ", state='" + state + '\'' +
               ", zipcode='" + zipcode + '\'' +
               ", country=" + country +
               '}';
   }
}
