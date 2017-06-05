package org.agoncal.application.petstore.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.agoncal.application.petstore.model.Item;
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
@Path("/items")
@Loggable
@Api("Item")
public class ItemEndpoint
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
   @ApiOperation("Creates a new item")
   public Response create(Item entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(ItemEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   @ApiOperation("Deletes an item by its id")
   public Response deleteById(@PathParam("id") Long id)
   {
      Item entity = em.find(Item.class, id);
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
   @ApiOperation("Finds an item by its id")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Item> findByIdQuery = em.createQuery("SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.product WHERE i.id = :entityId ORDER BY i.id", Item.class);
      findByIdQuery.setParameter("entityId", id);
      Item entity;
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
   @ApiOperation("Lists all items")
   public List<Item> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<Item> findAllQuery = em.createQuery("SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.product ORDER BY i.id", Item.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<Item> results = findAllQuery.getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes( {"application/xml", "application/json"})
   @ApiOperation("Updates an item")
   public Response update(Item entity)
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
