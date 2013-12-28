package org.agoncal.application.petstore.view.admin;

import org.agoncal.application.petstore.model.Country;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing bean for Country entities.
 * <p/>
 * This class provides CRUD functionality for all Country entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class CountryBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Country entities
    */

    private Long id;
    private Country country;

   /*
    * Support searching Country entities with pagination
    */
    private int page;
    private long count;
    private List<Country> pageItems;

    private Country example = new Country();

    @Inject
    private Conversation conversation;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

   /*
    * Support listing and POSTing back Category entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

    @Resource
    private SessionContext sessionContext;

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

    private Country add = new Country();

    // ======================================
    // =              Public Methods        =
    // ======================================

    public String create() {

        this.conversation.begin();
        return "create?faces-redirect=true";
    }

    public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (this.conversation.isTransient()) {
            this.conversation.begin();
        }

        if (this.id == null) {
            this.country = this.example;
        } else {
            this.country = findById(getId());
        }
    }

    public Country findById(Long id) {

        return this.entityManager.find(Country.class, id);
    }

   /*
    * Support updating and deleting Country entities
    */

    public String update() {
        this.conversation.end();

        try {
            if (this.id == null) {
                this.entityManager.persist(this.country);
                return "search?faces-redirect=true";
            } else {
                this.entityManager.merge(this.country);
                return "view?faces-redirect=true&id=" + this.country.getId();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        this.conversation.end();

        try {
            Country deletableEntity = findById(getId());

            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
            return "search?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public void search() {
        this.page = 0;
    }

    public void paginate() {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

        // Populate this.count

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Country> root = countCriteria.from(Country.class);
        countCriteria = countCriteria.select(builder.count(root)).where(
                getSearchPredicates(root));
        this.count = this.entityManager.createQuery(countCriteria)
                .getSingleResult();

        // Populate this.pageItems

        CriteriaQuery<Country> criteria = builder.createQuery(Country.class);
        root = criteria.from(Country.class);
        TypedQuery<Country> query = this.entityManager.createQuery(criteria
                .select(root).where(getSearchPredicates(root)));
        query.setFirstResult(this.page * getPageSize()).setMaxResults(
                getPageSize());
        this.pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Country> root) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        String isoCode = this.example.getIsoCode();
        if (isoCode != null && !"".equals(isoCode)) {
            predicatesList.add(builder.like(root.<String>get("isoCode"), '%' + isoCode + '%'));
        }
        String name = this.example.getName();
        if (name != null && !"".equals(name)) {
            predicatesList.add(builder.like(root.<String>get("name"), '%' + name + '%'));
        }
        String printableName = this.example.getPrintableName();
        if (printableName != null && !"".equals(printableName)) {
            predicatesList.add(builder.like(root.<String>get("printableName"), '%' + printableName + '%'));
        }
        String iso3 = this.example.getIso3();
        if (iso3 != null && !"".equals(iso3)) {
            predicatesList.add(builder.like(root.<String>get("iso3"), '%' + iso3 + '%'));
        }
        String numcode = this.example.getNumcode();
        if (numcode != null && !"".equals(numcode)) {
            predicatesList.add(builder.like(root.<String>get("numcode"), '%' + numcode + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public List<Country> getAll() {

        CriteriaQuery<Country> criteria = this.entityManager
                .getCriteriaBuilder().createQuery(Country.class);
        return this.entityManager.createQuery(
                criteria.select(criteria.from(Country.class))).getResultList();
    }

    // ======================================
    // =         Getters & setters          =
    // ======================================

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getCountry() {
        return this.country;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return 10;
    }

    public Country getExample() {
        return this.example;
    }

    public void setExample(Country example) {
        this.example = example;
    }

    public List<Country> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }


    public Country getAdd() {
        return this.add;
    }

    public Country getAdded() {
        Country added = this.add;
        this.add = new Country();
        return added;
    }

    // ======================================
    // =            Inner Class             =
    // ======================================

    public Converter getConverter() {

        final CountryBean ejbProxy = this.sessionContext.getBusinessObject(CountryBean.class);

        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context,
                                      UIComponent component, String value) {

                return ejbProxy.findById(Long.valueOf(value));
            }

            @Override
            public String getAsString(FacesContext context,
                                      UIComponent component, Object value) {

                if (value == null) {
                    return "";
                }

                return String.valueOf(((Country) value).getId());
            }
        };
    }
}