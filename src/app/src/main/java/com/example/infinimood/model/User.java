package com.example.infinimood.model;

/**
 *  User.java
 *  User class
 */

public class User {

    private String UserID;
    private String Username;

    public User() {}

    public User(String Uid, String Username){
        this.UserID = Uid;
        this.Username = Username;

    }

    public String getUsername() { return Username; }
    public String getUserID() { return UserID; }
}