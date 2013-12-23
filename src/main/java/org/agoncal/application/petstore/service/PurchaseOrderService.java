package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.*;
import org.agoncal.application.petstore.exception.ValidationException;
import org.agoncal.application.petstore.util.Loggable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Stateless
@Loggable
public class PurchaseOrderService implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private EntityManager em;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public PurchaseOrder createOrder(@NotNull Customer customer, @NotNull CreditCard creditCard, final List<ShoppingCartItem> cartItems) {

        // OMake sure the object is valid
        if (cartItems == null || cartItems.size() == 0)
            throw new ValidationException("Shopping cart is empty"); // TODO exception bean validation

        // Creating the order
        PurchaseOrder order = new PurchaseOrder(em.merge(customer), creditCard, customer.getHomeAddress());

        // From the shopping cart we create the order lines
        List<OrderLine> orderLines = new ArrayList<>();

        for (ShoppingCartItem cartItem : cartItems) {
            orderLines.add(new OrderLine(cartItem.getQuantity(), em.merge(cartItem.getItem())));
        }
        order.setOrderLines(orderLines);

        // Persists the object to the database
        em.persist(order);

        return order;
    }

    public PurchaseOrder findOrder(@NotNull Long orderId) {
        return em.find(PurchaseOrder.class, orderId);
    }

    public List<PurchaseOrder> findAllOrders() {
        TypedQuery<PurchaseOrder> typedQuery = em.createNamedQuery(PurchaseOrder.FIND_ALL, PurchaseOrder.class);
        return typedQuery.getResultList();
    }

    public void removeOrder(@NotNull PurchaseOrder order) {
        em.remove(em.merge(order));
    }
}
