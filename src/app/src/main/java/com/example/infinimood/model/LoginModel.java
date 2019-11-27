package com.example.infinimood.model;

import java.util.Observable;

public class LoginModel extends Observable {

    private String email = "";
    private String password = "";

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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
