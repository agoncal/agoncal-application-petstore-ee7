package org.agoncal.application.petstore.view.shopping;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Named
@SessionScoped
public class CredentialsBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private String login;
    private String password;
    private String password2;

    // ======================================
    // =         Getters & setters          =
    // ======================================

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
