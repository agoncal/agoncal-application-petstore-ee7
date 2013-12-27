package org.agoncal.application.petstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Entity
@Cacheable
@NamedQueries({
        // TODO fetch doesn't work with GlassFish
//        @NamedQuery(name = Product.FIND_BY_CATEGORY_NAME, query = "SELECT p FROM Product p LEFT JOIN FETCH p.items LEFT JOIN FETCH p.category WHERE p.category.name = :pname"),
        @NamedQuery(name = Product.FIND_BY_CATEGORY_NAME, query = "SELECT p FROM Product p WHERE p.category.name = :pname"),
        @NamedQuery(name = Product.FIND_ALL, query = "SELECT p FROM Product p")
})
@XmlRootElement
public class Product {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private int version;
    @Column(nullable = false, length = 30)
    @NotNull
    @Size(min = 1, max = 30)
    private String name;
    @Column(nullable = false)
    private String description;
    @ManyToOne
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

    public Product() {
    }

    public Product(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // ======================================
    // =         Getters & setters          =
    // ======================================

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (!name.equals(product.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}