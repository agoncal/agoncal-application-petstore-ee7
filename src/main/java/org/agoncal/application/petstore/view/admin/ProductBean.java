package org.agoncal.application.petstore.view.admin;

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.model.Product;

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
 * Backing bean for Product entities.
 * <p/>
 * This class provides CRUD functionality for all Product entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ProductBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Product entities
    */

    private Long id;
    private Product product;

   /*
    * Support searching Product entities with pagination
    */
    private int page;
    private long count;
    private List<Product> pageItems;

    private Product example = new Product();

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
    private Product add = new Product();

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
            this.product = this.example;
        } else {
            this.product = findById(getId());
        }
    }

    public Product findById(Long id) {

        return this.entityManager.find(Product.class, id);
    }

   /*
    * Support updating and deleting Product entities
    */

    public String update() {
        this.conversation.end();

        try {
            if (this.id == null) {
                this.entityManager.persist(this.product);
                return "search?faces-redirect=true";
            } else {
                this.entityManager.merge(this.product);
                return "view?faces-redirect=true&id=" + this.product.getId();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        this.conversation.end();

        try {
            Product deletableEntity = findById(getId());

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
        Root<Product> root = countCriteria.from(Product.class);
        countCriteria = countCriteria.select(builder.count(root)).where(
                getSearchPredicates(root));
        this.count = this.entityManager.createQuery(countCriteria)
                .getSingleResult();

        // Populate this.pageItems

        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        root = criteria.from(Product.class);
        TypedQuery<Product> query = this.entityManager.createQuery(criteria
                .select(root).where(getSearchPredicates(root)));
        query.setFirstResult(this.page * getPageSize()).setMaxResults(
                getPageSize());
        this.pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Product> root) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        String name = this.example.getName();
        if (name != null && !"".equals(name)) {
            predicatesList.add(builder.like(root.<String>get("name"), '%' + name + '%'));
        }
        String description = this.example.getDescription();
        if (description != null && !"".equals(description)) {
            predicatesList.add(builder.like(root.<String>get("description"), '%' + description + '%'));
        }
        Category category = this.example.getCategory();
        if (category != null) {
            predicatesList.add(builder.equal(root.get("category"), category));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public List<Product> getAll() {

        CriteriaQuery<Product> criteria = this.entityManager
                .getCriteriaBuilder().createQuery(Product.class);
        return this.entityManager.createQuery(
                criteria.select(criteria.from(Product.class))).getResultList();
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

    public Product getProduct() {
        return this.product;
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

    public Product getExample() {
        return this.example;
    }

    public void setExample(Product example) {
        this.example = example;
    }

    public List<Product> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }


    public Product getAdd() {
        return this.add;
    }

    public Product getAdded() {
        Product added = this.add;
        this.add = new Product();
        return added;
    }

    // ======================================
    // =            Inner Class             =
    // ======================================

    public Converter getConverter() {

        final ProductBean ejbProxy = this.sessionContext.getBusinessObject(ProductBean.class);

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

                return String.valueOf(((Product) value).getId());
            }
        };
    }
}