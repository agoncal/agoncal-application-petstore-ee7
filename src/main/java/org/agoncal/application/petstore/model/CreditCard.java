package org.agoncal.application.petstore.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Embeddable
public class CreditCard implements Serializable
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Column(length = 30, name = "credit_card_number", nullable = false)
   @NotNull
   @Size(min = 1, max = 30)
   private String creditCardNumber;

   @Enumerated
   @Column(name = "credit_card_type")
   @NotNull
   private CreditCardType creditCardType;

   @Column(length = 5, name = "credit_card_expiry_date", nullable = false)
   @NotNull
   @Size(min = 1, max = 5)
   private String creditCardExpDate;

   // ======================================
   // =            Constructors            =
   // ======================================

   public CreditCard()
   {
   }

   public CreditCard(String creditCardNumber, CreditCardType creditCardType, String creditCardExpDate)
   {
      this.creditCardNumber = creditCardNumber;
      this.creditCardType = creditCardType;
      this.creditCardExpDate = creditCardExpDate;
   }

   // ======================================
   // =         Getters & setters          =
   // ======================================

   public String getCreditCardNumber()
   {
      return creditCardNumber;
   }

   public void setCreditCardNumber(String creditCardNumber)
   {
      this.creditCardNumber = creditCardNumber;
   }

   public CreditCardType getCreditCardType()
   {
      return creditCardType;
   }

   public void setCreditCardType(CreditCardType creditCardType)
   {
      this.creditCardType = creditCardType;
   }

   public String getCreditCardExpDate()
   {
      return creditCardExpDate;
   }

   public void setCreditCardExpDate(String creditCardExpDate)
   {
      this.creditCardExpDate = creditCardExpDate;
   }

   // ======================================
   // =   Methods hash, equals, toString   =
   // ======================================

   @Override
   public final boolean equals(Object o)
   {
      if (!(o instanceof CreditCard))
      {
         return false;
      }

      CreditCard that = (CreditCard) o;

      return new EqualsBuilder()
               .append(creditCardNumber, that.creditCardNumber)
               .append(creditCardType, that.creditCardType)
               .append(creditCardExpDate, that.creditCardExpDate)
               .isEquals();
   }

   @Override
   public final int hashCode()
   {
      return new HashCodeBuilder(17, 37)
               .append(creditCardNumber)
               .append(creditCardType)
               .append(creditCardExpDate)
               .toHashCode();
   }

   @Override
   public String toString()
   {
      return new ToStringBuilder(this)
               .append("creditCardNumber", creditCardNumber)
               .append("creditCardType", creditCardType)
               .append("creditCardExpDate", creditCardExpDate)
               .toString();
   }
}
