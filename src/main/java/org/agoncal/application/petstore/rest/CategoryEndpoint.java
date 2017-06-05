package org.agoncal.application.petstore.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.util.Loggable;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Stateless
@Path("/categories")
@Loggable
@Api("Category")
public class CategoryEndpoint
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @PersistenceContext(unitName = "applicationPetstorePU")
   private EntityManager em;

   // ======================================
   // =          Business methods          =
   // ======================================

   @POST
   @Consumes( {"application/xml", "application/json"})
   @ApiOperation("Creates a category")
   public Response create(Category entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(CategoryEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   @ApiOperation("Deletes a category by id")
   public Response deleteById(@PathParam("id") Long id)
   {
      Category entity = em.find(Category.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces( {"application/xml", "application/json"})
   @ApiOperation("Finds a category given an identifier")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Category> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Category c WHERE c.id = :entityId ORDER BY c.id", Category.class);
      findByIdQuery.setParameter("entityId", id);
      Category entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.ok(entity).build();
   }

   @GET
   @Produces( {"application/xml", "application/json"})
   @ApiOperation("Lists all the categories")
   public List<Category> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<Category> findAllQuery = em.createQuery("SELECT DISTINCT c FROM Category c ORDER BY c.id", Category.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<Category> results = findAllQuery.getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes( {"application/xml", "application/json"})
   @ApiOperation("Updates a category")
   public Response update(Category entity)
   {
      try
      {
         entity = em.merge(entity);
      }
      catch (OptimisticLockException e)
      {
         return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
      }

      return Response.noContent().build();
   }
}
