package org.agoncal.application.petstore.view.shopping;

import org.agoncal.application.petstore.model.Item;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

public class ShoppingCartItem
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @NotNull
   private Item item;
   @NotNull
   @Min(1)
   private Integer quantity;

   // ======================================
   // =            Constructors            =
   // ======================================

   public ShoppingCartItem(Item item, Integer quantity)
   {
      this.item = item;
      this.quantity = quantity;
   }

   // ======================================
   // =              Public Methods        =
   // ======================================

   public Float getSubTotal()
   {
      return item.getUnitCost() * quantity;
   }

   // ======================================
   // =         Getters & setters          =
   // ======================================

   public Item getItem()
   {
      return item;
   }

   public void setItem(Item item)
   {
      this.item = item;
   }

   public Integer getQuantity()
   {
      return quantity;
   }

   public void setQuantity(Integer quantity)
   {
      this.quantity = quantity;
   }

   // ======================================
   // =   Methods hash, equals, toString   =
   // ======================================

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ShoppingCartItem cartItem = (ShoppingCartItem) o;

      if (!item.equals(cartItem.item)) return false;
      if (!quantity.equals(cartItem.quantity)) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = item.hashCode();
      result = 31 * result + quantity.hashCode();
      return result;
   }

   @Override
   public String toString()
   {
      final StringBuilder sb = new StringBuilder();
      sb.append("CartItem");
      sb.append("{item='").append(item).append('\'');
      sb.append(", quantity='").append(quantity).append('\'');
      sb.append('}');
      return sb.toString();
   }
}