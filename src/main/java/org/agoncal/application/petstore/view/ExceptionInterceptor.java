package org.agoncal.application.petstore.view;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 *         This interceptor catches exception and displayes them in a JSF page
 */

@Interceptor
@CatchException
public class ExceptionInterceptor implements Serializable {

    @Inject
    private Logger log;

    @AroundInvoke
    public Object catchException(InvocationContext ic) throws Exception {
        try {
            return ic.proceed();
        } catch (Exception e) {
            addErrorMessage(e.getMessage());
            log.severe("/!\\ " + ic.getTarget().getClass().getName() + " - " + ic.getMethod().getName() + " - " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // TODO to refactor with Controller methods
    protected void addErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
}
