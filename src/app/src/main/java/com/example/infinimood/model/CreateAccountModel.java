package com.example.infinimood.model;

import java.util.Observable;

/**
 * CreateAccountModel.java
 * Class that stores the data for creating an account
 */
public class CreateAccountModel extends Observable {

    private String username = "";
    private String email = "";
    private String password = "";
    private String passwordRepeat = "";

    /**
     * setUsername
     * @param username String - username to set
     */
    public void setUsername(String username) {
        if (username != null && !this.username.equals(username)) {
            this.username = username;
            setChanged();
        }
    }

    /**
     * setEmail
     * @param email String - email to set
     */
    public void setEmail(String email) {
        if (email != null && !this.email.equals(email)) {
            this.email = email;
            setChanged();
        }
    }

    /**
     * setPassword
     * @param password String - password to set
     */
    public void setPassword(String password) {
        if (password != null && !this.password.equals(password)) {
            this.password = password;
            setChanged();
        }
    }

    /**
     * setPasswordRepeat
     * @param passwordRepeat String - repeat password to set
     */
    public void setPasswordRepeat(String passwordRepeat) {
        if (passwordRepeat != null && !this.passwordRepeat.equals(passwordRepeat)) {
            this.passwordRepeat = passwordRepeat;
            setChanged();
        }
    }

    /**
     * getUsername
     * @return String - username
     */
    public String getUsername() {
        return username;
    }

    /**
     * getEmail
     * @return String - email
     */
    public String getEmail() {
        return email;
    }

    /**
     * getPassword
     * @return String - password
     */
    public String getPassword() {
        return password;
    }

    /**
     * getPasswordRepeat
     * @return String - repeat Password
     */
    public String getPasswordRepeat() {
        return passwordRepeat;
    }
}
