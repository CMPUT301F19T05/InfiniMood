package com.example.infinimood.model;

import java.util.Observable;

/**
 * LoginModel.java
 * Class that models data needed for logging in
 */
public class LoginModel extends Observable {

    private String email = "";
    private String password = "";

    /**
     * setEmail
     * @param email email to set
     */
    public void setEmail(String email) {
        if (email != null && !this.email.equals(email)) {
            this.email = email;
            setChanged();
        }
    }

    /**
     * setPassword
     * @param password password to set
     */
    public void setPassword(String password) {
        if (password != null && !this.password.equals(password)) {
            this.password = password;
            setChanged();
        }
    }

    /**
     * getEmail
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * getPassword
     * @return password
     */
    public String getPassword() {
        return password;
    }

}
