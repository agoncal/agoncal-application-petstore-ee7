package org.agoncal.application.petstore.web;

import org.agoncal.application.petstore.domain.Customer;
import org.agoncal.application.petstore.service.CustomerService;
import org.agoncal.application.petstore.util.Loggable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.login.CredentialException;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Named
@SessionScoped
@Loggable
@CatchException
public class AccountController extends Controller implements Serializable {

    private final Logger logger = Logger.getLogger(getClass().getName());

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private CustomerService customerService;

    @Inject
    private Credentials credentials;

    @Inject
    private Conversation conversation;

    private Customer loggedinCustomer;

    @Inject
    private HttpServletRequest servletRequest;


    // ======================================
    // =              Public Methods        =
    // ======================================

    public String doLogin() throws LoginException {
        if ("".equals(credentials.getLogin())) {
            addWarningMessage("id_filled");
            return null;
        }
        if ("".equals(credentials.getPassword())) {
            addWarningMessage("pwd_filled");
            return null;
        }
        try {
            servletRequest.login(credentials.getLogin(), credentials.getPassword());
        } catch (Exception e) {
            String msg = "Authentication failure for login '" + credentials.getLogin() + "'";
            logger.log(Level.WARNING, msg, e);
            CredentialException ce = new CredentialException(msg);
            ce.initCause(e);
            throw ce;
        }

        return "main.faces";
    }

    public String doCreateNewAccount() {

        // Login has to be unique
        if (customerService.doesLoginAlreadyExist(credentials.getLogin())) {
            addWarningMessage("login_exists");
            return null;
        }

        // Id and password must be filled
        if ("".equals(credentials.getLogin()) || "".equals(credentials.getPassword()) || "".equals(credentials.getPassword2())) {
            addWarningMessage("id_pwd_filled");
            return null;
        } else if (!credentials.getPassword().equals(credentials.getPassword2())) {
            addWarningMessage("both_pwd_same");
            return null;
        }

        // Login and password are ok
        loggedinCustomer = new Customer();
        loggedinCustomer.setLogin(credentials.getLogin());
        loggedinCustomer.setPassword(loggedinCustomer.digestPassword(credentials.getPassword()));

        return "createaccount.faces";
    }

    public String doCreateCustomer() {
        loggedinCustomer = customerService.createCustomer(loggedinCustomer);
        return "main.faces";
    }


    public String doLogout() {
        try {
            servletRequest.logout();
        } catch (ServletException e) {
            logger.log(Level.WARNING, "Exception logging out", e);
        }
        loggedinCustomer = null;
        // Stop conversation
        if (!conversation.isTransient()) {
            conversation.end();
        }
        addInformationMessage("been_loggedout");
        return "main.faces";
    }

    public String doUpdateAccount() {
        loggedinCustomer = customerService.updateCustomer(loggedinCustomer);
        addInformationMessage("account_updated");
        return "showaccount.faces";
    }

    public boolean isLoggedIn() {
        return servletRequest.getUserPrincipal() != null;
    }

    @Produces
    @LoggedIn
    public Customer getLoggedinCustomer() {
        if (loggedinCustomer == null) {
            String login = servletRequest.getRemoteUser();
            if (login == null) {
                return null;
            }
            return customerService.findCustomer(login);
        } else {
            return loggedinCustomer;
        }
    }

    public void setLoggedinCustomer(Customer loggedinCustomer) {
        this.loggedinCustomer = loggedinCustomer;
    }
}
