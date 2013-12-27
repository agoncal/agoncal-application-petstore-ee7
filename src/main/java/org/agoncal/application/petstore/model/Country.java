package org.agoncal.application.petstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Entity
@Cacheable
public class Country {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id @GeneratedValue
    private Long id;
    @NotNull
    @Size(min = 2, max = 2)
    private String isoCode;
    @Column(nullable = false, length = 80)
    @NotNull
    @Size(min = 2, max = 80)
    private String name;
    @Column(nullable = false, length = 80)
    @NotNull
    @Size(min = 2, max = 80)
    private String printableName;
    @Column(length = 3)
    @Size(min = 3, max = 3)
    private String iso3;
    @Column(length = 3)
    @Size(min = 3, max = 3)
    private String numcode;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Country() {
    }

    public Country(String isoCode, String name, String printableName) {
        this.isoCode = isoCode;
        this.name = name;
        this.printableName = printableName;
    }

    // ======================================
    // =         Getters & setters          =
    // ======================================


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrintableName() {
        return printableName;
    }

    public void setPrintableName(String printableName) {
        this.printableName = printableName;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    // ======================================
    // =   Methods hash, equals, toString   =
    // ======================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (iso3 != null ? !iso3.equals(country.iso3) : country.iso3 != null) return false;
        if (!isoCode.equals(country.isoCode)) return false;
        if (!name.equals(country.name)) return false;
        if (numcode != null ? !numcode.equals(country.numcode) : country.numcode != null) return false;
        if (!printableName.equals(country.printableName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = isoCode.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + printableName.hashCode();
        result = 31 * result + (iso3 != null ? iso3.hashCode() : 0);
        result = 31 * result + (numcode != null ? numcode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return printableName;
    }
}
