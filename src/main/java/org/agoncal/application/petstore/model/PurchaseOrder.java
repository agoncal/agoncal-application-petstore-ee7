package org.agoncal.application.petstore.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
   public final boolean equals(Object o)
   {
      if (!(o instanceof PurchaseOrder))
      {
         return false;
      }

      PurchaseOrder that = (PurchaseOrder) o;

      return new EqualsBuilder()
               .append(version, that.version)
               .append(id, that.id)
               .append(orderDate, that.orderDate)
               .append(totalWithoutVat, that.totalWithoutVat)
               .append(vatRate, that.vatRate)
               .append(vat, that.vat)
               .append(totalWithVat, that.totalWithVat)
               .append(discountRate, that.discountRate)
               .append(discount, that.discount)
               .append(total, that.total)
               .append(customer, that.customer)
               .append(orderLines, that.orderLines)
               .append(deliveryAddress, that.deliveryAddress)
               .append(creditCard, that.creditCard)
               .isEquals();
   }

   @Override
   public final int hashCode()
   {
      return new HashCodeBuilder(17, 37)
               .append(id)
               .append(version)
               .append(orderDate)
               .append(totalWithoutVat)
               .append(vatRate)
               .append(vat)
               .append(totalWithVat)
               .append(discountRate)
               .append(discount)
               .append(total)
               .append(customer)
               .append(orderLines)
               .append(deliveryAddress)
               .append(creditCard)
               .toHashCode();
   }

   @Override
   public String toString()
   {
      return new ToStringBuilder(this)
               .append("id", id)
               .append("version", version)
               .append("orderDate", orderDate)
               .append("totalWithoutVat", totalWithoutVat)
               .append("vatRate", vatRate)
               .append("vat", vat)
               .append("totalWithVat", totalWithVat)
               .append("discountRate", discountRate)
               .append("discount", discount)
               .append("total", total)
               .append("customer", customer)
               .append("orderLines", orderLines)
               .append("deliveryAddress", deliveryAddress)
               .append("creditCard", creditCard)
               .toString();
   }
}
