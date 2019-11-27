package com.example.infinimood.model;

import java.util.Observable;

public class CreateAccountModel extends Observable {

    private String username = "";
    private String email = "";
    private String password = "";
    private String passwordRepeat = "";

    public void setUsername(String username) {
        if (username != null && !this.username.equals(username)) {
            this.username = username;
            setChanged();
        }
    }

    public void setEmail(String email) {
        if (email != null && !this.email.equals(email)) {
            this.email = email;
            setChanged();
        }
    }

    public void setPassword(String password) {
        if (password != null && !this.password.equals(password)) {
            this.password = password;
            setChanged();
        }
    }

    public void setPasswordRepeat(String passwordRepeat) {
        if (passwordRepeat != null && !this.passwordRepeat.equals(passwordRepeat)) {
            this.passwordRepeat = passwordRepeat;
            setChanged();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

}
