package org.agoncal.application.petstore.view.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

import org.agoncal.application.petstore.model.PurchaseOrder;
import org.agoncal.application.petstore.util.Loggable;

/**
 * Backing bean for PurchaseOrder entities.
 * <p/>
 * This class provides CRUD functionality for all PurchaseOrder entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class PurchaseOrderBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving PurchaseOrder entities
    */

   private Long id;

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private PurchaseOrder purchaseOrder;

   public PurchaseOrder getPurchaseOrder()
   {
      return this.purchaseOrder;
   }

   public void setPurchaseOrder(PurchaseOrder purchaseOrder)
   {
      this.purchaseOrder = purchaseOrder;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(unitName = "applicationPetstorePU", type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
         this.conversation.setTimeout(1800000L);
      }

      if (this.id == null)
      {
         this.purchaseOrder = this.example;
      }
      else
      {
         this.purchaseOrder = findById(getId());
      }
   }

   public PurchaseOrder findById(Long id)
   {

      return this.entityManager.find(PurchaseOrder.class, id);
   }

   /*
    * Support updating and deleting PurchaseOrder entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.purchaseOrder);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.purchaseOrder);
            return "view?faces-redirect=true&id=" + this.purchaseOrder.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         PurchaseOrder deletableEntity = findById(getId());

         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching PurchaseOrder entities with pagination
    */

   private int page;
   private long count;
   private List<PurchaseOrder> pageItems;

   private PurchaseOrder example = new PurchaseOrder();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public PurchaseOrder getExample()
   {
      return this.example;
   }

   public void setExample(PurchaseOrder example)
   {
      this.example = example;
   }

   public String search()
   {
      this.page = 0;
      return null;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<PurchaseOrder> root = countCriteria.from(PurchaseOrder.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<PurchaseOrder> criteria = builder.createQuery(PurchaseOrder.class);
      root = criteria.from(PurchaseOrder.class);
      TypedQuery<PurchaseOrder> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<PurchaseOrder> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String street1 = this.example.getDeliveryAddress().getStreet1();
      if (street1 != null && !"".equals(street1))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("street1")), '%' + street1.toLowerCase() + '%'));
      }
      String street2 = this.example.getDeliveryAddress().getStreet2();
      if (street2 != null && !"".equals(street2))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("street2")), '%' + street2.toLowerCase() + '%'));
      }
      String city = this.example.getDeliveryAddress().getCity();
      if (city != null && !"".equals(city))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("city")), '%' + city.toLowerCase() + '%'));
      }
      String state = this.example.getDeliveryAddress().getState();
      if (state != null && !"".equals(state))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("state")), '%' + state.toLowerCase() + '%'));
      }
      String zipcode = this.example.getDeliveryAddress().getZipcode();
      if (zipcode != null && !"".equals(zipcode))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("zipcode")), '%' + zipcode.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<PurchaseOrder> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back PurchaseOrder entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<PurchaseOrder> getAll()
   {

      CriteriaQuery<PurchaseOrder> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(PurchaseOrder.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(PurchaseOrder.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PurchaseOrderBean ejbProxy = this.sessionContext.getBusinessObject(PurchaseOrderBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Long.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((PurchaseOrder) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private PurchaseOrder add = new PurchaseOrder();

   public PurchaseOrder getAdd()
   {
      return this.add;
   }

   public PurchaseOrder getAdded()
   {
      PurchaseOrder added = this.add;
      this.add = new PurchaseOrder();
      return added;
   }
}
