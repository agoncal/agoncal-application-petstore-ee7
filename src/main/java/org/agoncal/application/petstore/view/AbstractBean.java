package org.agoncal.application.petstore.view;

import org.agoncal.application.petstore.util.Loggable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Loggable
public abstract class AbstractBean {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private transient Logger logger;

    // ======================================
    // =          Protected Methods         =
    // ======================================

    private String getMessage(FacesContext facesContext, String msgKey, Object... args) {
        Locale locale = facesContext.getViewRoot().getLocale();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ResourceBundle bundle = ResourceBundle.getBundle("Messages", locale, classLoader);
        String msgValue = bundle.getString(msgKey);
        return MessageFormat.format(msgValue, args);
    }

    protected void addInformationMessage(String message, Object... args) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage(context, message, args), null));
    }

    protected void addWarningMessage(String message, Object... args) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage(context, message, args), null));
    }

    protected void addErrorMessage(String message, Object... args) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage(context, message, args), null));
    }

    protected String getParam(String param) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        return map.get(param);
    }

    protected Long getParamId(String param) {
        return Long.valueOf(getParam(param));
    }
}