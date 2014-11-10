package org.agoncal.application.petstore.model;

import org.agoncal.application.petstore.constraints.NotEmpty;
import org.agoncal.application.petstore.constraints.Price;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Entity
@Cacheable
@NamedQueries( {
      @NamedQuery(name = Item.FIND_BY_PRODUCT_ID, query = "SELECT i FROM Item i WHERE i.product.id = :productId"),
      @NamedQuery(name = Item.SEARCH, query = "SELECT i FROM Item i WHERE UPPER(i.name) LIKE :keyword OR UPPER(i.product.name) LIKE :keyword ORDER BY i.product.category.name, i.product.name"),
      @NamedQuery(name = Item.FIND_ALL, query = "SELECT i FROM Item i")
})
@XmlRootElement
public class Item implements Serializable
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

   @Column(length = 30, nullable = false)
   @NotNull
   @Size(min = 1, max = 30)
   private String name;

   @Column(length = 3000, nullable = false)
   @NotNull
   @Size(max = 3000)
   private String description;

   @Column(name = "image_path")
   @NotEmpty
   private String imagePath;

   @Column(name = "unit_cost", nullable = false)
   @NotNull
   @Price
   private Float unitCost;

   @ManyToOne(cascade = CascadeType.PERSIST)
   @JoinColumn(name = "product_fk", nullable = false)
   @XmlTransient
   private Product product;

   // ======================================
   // =             Constants              =
   // ======================================

   public static final String FIND_BY_PRODUCT_ID = "Item.findByProductId";
   public static final String SEARCH = "Item.search";
   public static final String FIND_ALL = "Item.findAll";

   // ======================================
   // =            Constructors            =
   // ======================================

   public Item()
   {
   }

   public Item(String name, Float unitCost, String imagePath, String description, Product product)
   {
      this.name = name;
      this.unitCost = unitCost;
      this.imagePath = imagePath;
      this.description = description;
      this.product = product;
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

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getImagePath()
   {
      return imagePath;
   }

   public void setImagePath(String imagePath)
   {
      this.imagePath = imagePath;
   }

   public Float getUnitCost()
   {
      return unitCost;
   }

   public void setUnitCost(Float unitCost)
   {
      this.unitCost = unitCost;
   }

   public Product getProduct()
   {
      return this.product;
   }

   public void setProduct(final Product product)
   {
      this.product = product;
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
      if (!(obj instanceof Item))
      {
         return false;
      }
      Item other = (Item) obj;
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
      if (name != null && !name.trim().isEmpty())
         result += ", name: " + name;
      if (description != null && !description.trim().isEmpty())
         result += ", description: " + description;
      if (imagePath != null && !imagePath.trim().isEmpty())
         result += ", imagePath: " + imagePath;
      if (unitCost != null)
         result += ", unitCost: " + unitCost;
      return result;
   }
}
