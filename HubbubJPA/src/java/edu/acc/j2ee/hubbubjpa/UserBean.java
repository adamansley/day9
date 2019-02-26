package edu.acc.j2ee.hubbubjpa;

import javax.persistence.Basic;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserBean {
    
    @Basic(optional = false)
    @NotNull
    @Pattern(regexp = "\\w{6,12}")
    private String username;
    
    @Basic(optional = false)
    @NotNull
    @Pattern(regexp = "[^<>'\"]{8,16}")
    private String password;
    
    public UserBean() {}
    
    public UserBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return String.format("UserBean[%s,%s]", username, password);
    }
}
