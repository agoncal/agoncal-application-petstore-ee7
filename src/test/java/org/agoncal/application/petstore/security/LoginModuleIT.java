package org.agoncal.application.petstore.security;

import org.agoncal.application.petstore.domain.Customer;
import org.agoncal.application.petstore.service.CustomerService;
import org.agoncal.application.petstore.util.ConfigPropertyProducer;
import org.agoncal.application.petstore.util.LoggingProducer;
import org.agoncal.application.petstore.web.Credentials;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * @author blep
 *         Date: 12/02/12
 *         Time: 11:59
 */
@Ignore
@RunWith(Arquillian.class)
public class LoginModuleIT {

    private CustomerService customerService;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(Credentials.class, SimpleCallbackHandler.class, LoggingProducer.class)
                .addClasses(LoginContextProducer.class, ConfigPropertyProducer.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }

    //@Produces
    public CustomerService produceMockCustomerService() {
        return customerService;
    }

    @Inject
    private Credentials credentials;

    @Inject
    @SessionScoped
    private LoginContext loginContext;

    @Before
    public void setUp() throws Exception {
        customerService = mock(CustomerService.class);
    }

    @Test
    public void testLogin() throws Exception {
        String login = "admin";
        String password = "password";

        credentials.setLogin(login);
        credentials.setPassword(password);

        when(customerService.findCustomer(login, password)).thenReturn(new Customer());

        loginContext.login();

        verify(customerService).findCustomer(login, password);
    }

    @Test(expected = LoginException.class)
    public void testWrongLogin() throws LoginException {
        String login = "Bob Marley";
        String password = "no woman no cry";

        credentials.setLogin(login);
        credentials.setPassword(password);

        try {
            loginContext.login();
        } catch (LoginException e) {
            assertEquals(e.getMessage(), "Authentication failed");
            verify(customerService).findCustomer(login, password);
            throw e;
        }

        fail();
    }
}
