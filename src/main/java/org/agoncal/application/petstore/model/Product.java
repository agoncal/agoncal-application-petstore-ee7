package org.agoncal.application.petstore.model;

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

@Entity
@Cacheable
@NamedQueries( {
      // TODO fetch doesn't work with GlassFish
      // @NamedQuery(name = Product.FIND_BY_CATEGORY_NAME, query =
      // "SELECT p FROM Product p LEFT JOIN FETCH p.items LEFT JOIN FETCH p.category WHERE p.category.name = :pname"),
      @NamedQuery(name = Product.FIND_BY_CATEGORY_NAME, query = "SELECT p FROM Product p WHERE p.category.name = :pname"),
      @NamedQuery(name = Product.FIND_ALL, query = "SELECT p FROM Product p")
})
@XmlRootElement
public class Product implements Serializable
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

   @ManyToOne(cascade = CascadeType.PERSIST)
   @JoinColumn(name = "category_fk", nullable = false)
   @XmlTransient
   private Category category;

   // ======================================
   // =             Constants              =
   // ======================================

   public static final String FIND_BY_CATEGORY_NAME = "Product.findByCategoryName";
   public static final String FIND_ALL = "Product.findAll";

   // ======================================
   // =            Constructors            =
   // ======================================

   public Product()
   {
   }

   public Product(String name, String description, Category category)
   {
      this.name = name;
      this.description = description;
      this.category = category;
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

   public Category getCategory()
   {
      return this.category;
   }

   public void setCategory(final Category category)
   {
      this.category = category;
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
      if (!(obj instanceof Product))
      {
         return false;
      }
      Product other = (Product) obj;
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
      return result;
   }
}
