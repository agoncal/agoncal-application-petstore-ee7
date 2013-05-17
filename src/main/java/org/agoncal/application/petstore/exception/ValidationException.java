package org.agoncal.application.petstore.exception;

import javax.ejb.ApplicationException;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 *         Thrown when data is not valid
 */

@ApplicationException(rollback = true)
public class ValidationException extends RuntimeException {

    // ======================================
    // =            Constructors            =
    // ======================================

    public ValidationException(String message) {
        super(message);
    }
}