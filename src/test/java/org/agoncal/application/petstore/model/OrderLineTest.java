package org.agoncal.application.petstore.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * @author Antonio Goncalves
 */
public class OrderLineTest {

    // ======================================
    // =              Methods               =
    // ======================================

    @Test
    public void shouldCheckEqualsAndHashCode() {

        // Checks equals and hashCode is valid
        EqualsVerifier.forClass(OrderLine.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().withIgnoredFields("version").withNonnullFields("quantity", "item").verify();
    }
}