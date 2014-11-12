package org.agoncal.application.petstore.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import org.agoncal.application.petstore.util.Loggable;

@Loggable
public abstract class AbstractService<T>
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @PersistenceContext(unitName = "applicationPetstorePU")
   protected EntityManager entityManager;

   private Class<T> entityClass;

   // ======================================
   // =            Constructors            =
   // ======================================

   public AbstractService()
   {
   }

   public AbstractService(Class<T> entityClass)
   {
      this.entityClass = entityClass;
   }

   // ======================================
   // =          Business methods          =
   // ======================================

   public T persist(T entity)
   {
      entityManager.persist(entity);
      return entity;
   }

   public T findById(Long id)
   {
      return entityManager.find(entityClass, id);
   }

   public void remove(T entity)
   {
      entityManager.remove(entityManager.merge(entity));
   }

   public T merge(T entity)
   {
      return entityManager.merge(entity);
   }

   public List<T> listAll(Integer startPosition, Integer maxResult)
   {
      TypedQuery<T> findAllQuery = getListAllQuery();
      if (startPosition != null)
{
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<T> results = findAllQuery.getResultList();
      return results;
   }

   public List<T> listAll()
   {
      return getListAllQuery().getResultList();
   }

   public TypedQuery<T> getListAllQuery()
   {
      CriteriaQuery<T> criteria = entityManager.getCriteriaBuilder().createQuery(entityClass);
      return entityManager.createQuery(criteria.select(criteria.from(entityClass)));
   }

   public long count(T example)
   {

      CriteriaBuilder builder = entityManager.getCriteriaBuilder();

      // Populate count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<T> root = countCriteria.from(entityClass);
      countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, example));
      long count = entityManager.createQuery(countCriteria).getSingleResult();
      return count;
   }

   public List<T> page(T example, int page, int pageSize)
   {

      CriteriaBuilder builder = entityManager.getCriteriaBuilder();

      // Populate pageItems

      CriteriaQuery<T> criteria = builder.createQuery(entityClass);
      Root<T> root = criteria.from(entityClass);
      TypedQuery<T> query = entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root, example)));
      query.setFirstResult(page * pageSize).setMaxResults(pageSize);
      List<T> pageItems = query.getResultList();
      return pageItems;

   }

   // ======================================
   // =         Protected methods          =
   // ======================================

   protected abstract Predicate[] getSearchPredicates(Root<T> root, T example);
}