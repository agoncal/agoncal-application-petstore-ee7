package org.agoncal.application.petstore.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "purchase_order")
@XmlRootElement
@NamedQueries( {
      @NamedQuery(name = PurchaseOrder.FIND_ALL, query = "SELECT o FROM PurchaseOrder o")
})
public class PurchaseOrder implements Serializable
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   @Version
   @Column(name = "version")
   private int version;

   @Column(name = "order_date", updatable = false)
   @Temporal(TemporalType.DATE)
   private Date orderDate;

   @Column
   private Float totalWithoutVat;

   @Column(name = "vat_rate")
   private Float vatRate;

   @Column
   private Float vat;

   @Column
   private Float totalWithVat;

   @Column(name = "discount_rate")
   private Float discountRate;

   @Column
   private Float discount;

   @Column
   private Float total;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "customer_fk", nullable = false)
   private Customer customer;

   @OneToMany//TODO (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinTable(name = "t_order_order_line",
         joinColumns = {@JoinColumn(name = "order_fk")},
         inverseJoinColumns = {@JoinColumn(name = "order_line_fk")})
   private Set<OrderLine> orderLines = new HashSet<OrderLine>();

   @Embedded
   @Valid
   private Address deliveryAddress = new Address();

   @Embedded
   @Valid
   private CreditCard creditCard = new CreditCard();

   // ======================================
   // =             Constants              =
   // ======================================

   public static final String FIND_ALL = "Order.findAll";

   // ======================================
   // =            Constructors            =
   // ======================================

   public PurchaseOrder()
   {
   }

   public PurchaseOrder(Customer customer, CreditCard creditCard, Address deliveryAddress)
   {
      this.customer = customer;
      this.creditCard = creditCard;
      this.deliveryAddress = deliveryAddress;
   }

   // ======================================
   // =         Lifecycle Methods          =
   // ======================================

   @PrePersist
   private void setDefaultData()
   {
      orderDate = new Date();
   }

   // ======================================
   // =         Getters & setters          =
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

   public Date getOrderDate()
   {
      return orderDate;
   }

   public void setOrderDate(Date orderDate)
   {
      this.orderDate = orderDate;
   }

   public Float getTotalWithoutVat()
   {
      return totalWithoutVat;
   }

   public void setTotalWithoutVat(Float totalWithoutVat)
   {
      this.totalWithoutVat = totalWithoutVat;
   }

   public Float getVatRate()
   {
      return vatRate;
   }

   public void setVatRate(Float vatRate)
   {
      this.vatRate = vatRate;
   }

   public Float getVat()
   {
      return vat;
   }

   public void setVat(Float vat)
   {
      this.vat = vat;
   }

   public Float getTotalWithVat()
   {
      return totalWithVat;
   }

   public void setTotalWithVat(Float totalWithVat)
   {
      this.totalWithVat = totalWithVat;
   }

   public Float getDiscountRate()
   {
      return discountRate;
   }

   public void setDiscountRate(Float discountRate)
   {
      this.discountRate = discountRate;
   }

   public Float getDiscount()
   {
      return discount;
   }

   public void setDiscount(Float discount)
   {
      this.discount = discount;
   }

   public Float getTotal()
   {
      return total;
   }

   public void setTotal(Float total)
   {
      this.total = total;
   }

   public Customer getCustomer()
   {
      return this.customer;
   }

   public void setCustomer(final Customer customer)
   {
      this.customer = customer;
   }

   public Set<OrderLine> getOrderLines()
   {
      return this.orderLines;
   }

   public void setOrderLines(final Set<OrderLine> orderLines)
   {
      this.orderLines = orderLines;
   }

   public Address getDeliveryAddress()
   {
      return deliveryAddress;
   }

   public void setDeliveryAddress(Address deliveryAddress)
   {
      this.deliveryAddress = deliveryAddress;
   }

   public CreditCard getCreditCard()
   {
      return creditCard;
   }

   public void setCreditCard(CreditCard creditCard)
   {
      this.creditCard = creditCard;
   }

   // ======================================
   // =   Methods hash, equals, toString   =
   // ======================================

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof PurchaseOrder))
      {
         return false;
      }
      PurchaseOrder other = (PurchaseOrder) obj;
      if (id != null)
      {
         if (!id.equals(other.id))
         {
            return false;
         }
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (id != null)
         result += "id: " + id;
      result += ", version: " + version;
      if (orderDate != null)
         result += ", orderDate: " + orderDate;
      if (totalWithoutVat != null)
         result += ", totalWithoutVat: " + totalWithoutVat;
      if (vatRate != null)
         result += ", vatRate: " + vatRate;
      if (vat != null)
         result += ", vat: " + vat;
      if (totalWithVat != null)
         result += ", totalWithVat: " + totalWithVat;
      if (discountRate != null)
         result += ", discountRate: " + discountRate;
      if (discount != null)
         result += ", discount: " + discount;
      if (total != null)
         result += ", total: " + total;
      return result;
   }
}
