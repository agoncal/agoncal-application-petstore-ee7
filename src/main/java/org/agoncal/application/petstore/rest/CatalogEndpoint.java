package org.agoncal.application.petstore.rest;

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.model.Product;
import org.agoncal.application.petstore.service.CatalogService;
import org.agoncal.application.petstore.util.Loggable;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import java.io.Serializable;
import java.net.URI;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Loggable
@Path("/catalog")
public class CatalogEndpoint implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @EJB //@Inject TODO @Inject should work instead of @EJB
    private CatalogService catalogService;

    @Context
    private UriInfo uriInfo;

    // ======================================
    // =          Business methods          =
    // ======================================

    @GET
    @Path("/categories")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Category> findAllCategories() {
        return catalogService.findAllCategories();
    }

    @GET
    @Path("/category/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Category findCategory(@PathParam("id") Long categoryId) {
        return catalogService.findCategory(categoryId);
    }

    @POST
    @Path("/category")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createCategory(JAXBElement<Category> xmlCategory) {
        Category category = catalogService.createCategory(xmlCategory.getValue());
        URI uri = uriInfo.getAbsolutePathBuilder().path(category.getId().toString()).build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("/category")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateCategory(JAXBElement<Category> xmlCategory) {
        Category category = catalogService.updateCategory(xmlCategory.getValue());
        URI uri = uriInfo.getAbsolutePathBuilder().path(category.getId().toString()).build();
        return Response.ok(uri).build();
    }

    @DELETE
    @Path("/category/{id}")
    public Response removeCategory(@PathParam("id") Long categoryId) {
        catalogService.removeCategory(categoryId);
        return Response.noContent().build();
    }

    @GET
    @Path("/products")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Product> findAllProducts() {
        return catalogService.findAllProducts();
    }

    @GET
    @Path("/product/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Product findProduct(@PathParam("id") Long productId) {
        return catalogService.findProduct(productId);
    }

    @POST
    @Path("/product")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createProduct(JAXBElement<Product> xmlProduct) {
        Product product = catalogService.createProduct(xmlProduct.getValue());
        URI uri = uriInfo.getAbsolutePathBuilder().path(product.getId().toString()).build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("/product")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateProduct(JAXBElement<Product> xmlProduct) {
        Product product = catalogService.updateProduct(xmlProduct.getValue());
        URI uri = uriInfo.getAbsolutePathBuilder().path(product.getId().toString()).build();
        return Response.ok(uri).build();
    }

    @DELETE
    @Path("/product/{id}")
    public Response removeProduct(@PathParam("id") Long productId) {
        catalogService.removeProduct(productId);
        return Response.noContent().build();
    }

    @GET
    @Path("/items")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Item> findAllItems() {
        return catalogService.findAllItems();
    }

    @GET
    @Path("/item/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Item findItem(@PathParam("id") Long itemId) {
        return catalogService.findItem(itemId);
    }

    @POST
    @Path("/item")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createItem(JAXBElement<Item> xmlItem) {
        Item item = catalogService.createItem(xmlItem.getValue());
        URI uri = uriInfo.getAbsolutePathBuilder().path(item.getId().toString()).build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("/item")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateItem(JAXBElement<Item> xmlItem) {
        Item item = catalogService.updateItem(xmlItem.getValue());
        URI uri = uriInfo.getAbsolutePathBuilder().path(item.getId().toString()).build();
        return Response.ok(uri).build();
    }

    @DELETE
    @Path("/item/{id}")
    public Response removeItem(@PathParam("id") Long itemId) {
        catalogService.removeItem(itemId);
        return Response.noContent().build();
    }

}
