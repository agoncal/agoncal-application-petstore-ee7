package org.agoncal.application.petstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Entity
@Cacheable
@NamedQueries({
         // TODO fetch doesn't work with GlassFIsh
         // @NamedQuery(name = Category.FIND_BY_NAME, query =
         // "SELECT c FROM Category c LEFT JOIN FETCH c.products WHERE c.name = :pname"),
         @NamedQuery(name = Category.FIND_BY_NAME, query = "SELECT c FROM Category c WHERE c.name = :pname"),
         @NamedQuery(name = Category.FIND_ALL, query = "SELECT c FROM Category c")
})
@XmlRootElement
public class Category implements Serializable
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

   // ======================================
   // = Constants =
   // ======================================

   public static final String FIND_BY_NAME = "Category.findByName";
   public static final String FIND_ALL = "Category.findAll";

   // ======================================
   // = Constructors =
   // ======================================

   public Category()
   {
   }

   public Category(String name, String description)
   {
      this.name = name;
      this.description = description;
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

   // ======================================
   // = Methods hash, equals, toString =
   // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
   public String toString() {
      return name;
   }
}
