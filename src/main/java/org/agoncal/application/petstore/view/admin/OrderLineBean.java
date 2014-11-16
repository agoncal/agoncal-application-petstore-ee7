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

import org.agoncal.application.petstore.model.OrderLine;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.util.Loggable;

/**
 * Backing bean for OrderLine entities.
 * <p/>
 * This class provides CRUD functionality for all OrderLine entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class OrderLineBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving OrderLine entities
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

   private OrderLine orderLine;

   public OrderLine getOrderLine()
   {
      return this.orderLine;
   }

   public void setOrderLine(OrderLine orderLine)
   {
      this.orderLine = orderLine;
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
         this.orderLine = this.example;
      }
      else
      {
         this.orderLine = findById(getId());
      }
   }

   public OrderLine findById(Long id)
   {

      return this.entityManager.find(OrderLine.class, id);
   }

   /*
    * Support updating and deleting OrderLine entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.orderLine);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.orderLine);
            return "view?faces-redirect=true&id=" + this.orderLine.getId();
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
         OrderLine deletableEntity = findById(getId());

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
    * Support searching OrderLine entities with pagination
    */

   private int page;
   private long count;
   private List<OrderLine> pageItems;

   private OrderLine example = new OrderLine();

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

   public OrderLine getExample()
   {
      return this.example;
   }

   public void setExample(OrderLine example)
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
      Root<OrderLine> root = countCriteria.from(OrderLine.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<OrderLine> criteria = builder.createQuery(OrderLine.class);
      root = criteria.from(OrderLine.class);
      TypedQuery<OrderLine> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<OrderLine> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      Integer quantity = this.example.getQuantity();
      if (quantity != null && quantity.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("quantity"), quantity));
      }
      Item item = this.example.getItem();
      if (item != null)
      {
         predicatesList.add(builder.equal(root.get("item"), item));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<OrderLine> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back OrderLine entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<OrderLine> getAll()
   {

      CriteriaQuery<OrderLine> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(OrderLine.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(OrderLine.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final OrderLineBean ejbProxy = this.sessionContext.getBusinessObject(OrderLineBean.class);

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

            return String.valueOf(((OrderLine) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private OrderLine add = new OrderLine();

   public OrderLine getAdd()
   {
      return this.add;
   }

   public OrderLine getAdded()
   {
      OrderLine added = this.add;
      this.add = new OrderLine();
      return added;
   }
}
