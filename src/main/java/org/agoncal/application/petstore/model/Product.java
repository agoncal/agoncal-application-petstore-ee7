package org.agoncal.application.petstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Cacheable
@NamedQueries({
         // TODO fetch doesn't work with GlassFish
         // @NamedQuery(name = Product.FIND_BY_CATEGORY_NAME, query =
         // "SELECT p FROM Product p LEFT JOIN FETCH p.items LEFT JOIN FETCH p.category WHERE p.category.name =
         // :pname"),
         @NamedQuery(name = Product.FIND_BY_CATEGORY_NAME, query = "SELECT p FROM Product p WHERE p.category.name = :pname"),
         @NamedQuery(name = Product.FIND_ALL, query = "SELECT p FROM Product p")
})
@XmlRootElement
public class Product implements Serializable
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
   // = Constants =
   // ======================================

   public static final String FIND_BY_CATEGORY_NAME = "Product.findByCategoryName";
   public static final String FIND_ALL = "Product.findAll";

   // ======================================
   // = Constructors =
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
   // = Methods hash, equals, toString =
   // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name) && description.equals(product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
   public String toString()
   {
      return name;
   }
}
