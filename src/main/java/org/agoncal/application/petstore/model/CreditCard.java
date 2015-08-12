package org.agoncal.application.petstore.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Embeddable
public class CreditCard implements Serializable
{

   // ======================================
   // = Attributes =
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
   // = Constructors =
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
   // = Getters & setters =
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
   // = Methods hash, equals, toString =
   // ======================================

   @Override
   public final boolean equals(Object o)
   {
      if (this == o)
         return true;
      if (!(o instanceof CreditCard))
         return false;
      CreditCard that = (CreditCard) o;
      return Objects.equals(creditCardNumber, that.creditCardNumber) &&
               Objects.equals(creditCardType, that.creditCardType) &&
               Objects.equals(creditCardExpDate, that.creditCardExpDate);
   }

   @Override
   public final int hashCode()
   {
      return Objects.hash(creditCardNumber, creditCardType, creditCardExpDate);
   }

   @Override
   public String toString()
   {
      return "CreditCard{" +
               "creditCardNumber='" + creditCardNumber + '\'' +
               ", creditCardType=" + creditCardType +
               ", creditCardExpDate='" + creditCardExpDate + '\'' +
               '}';
   }
}
