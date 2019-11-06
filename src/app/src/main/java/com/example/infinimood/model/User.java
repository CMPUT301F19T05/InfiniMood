package com.example.infinimood.model;

/**
 *  User.java
 *  User class
 */

public class User {

    private String userID;
    private String username;

    public User() {}

    public User(String Uid, String username){
        this.userID = Uid;
        this.username = username;
    }

    public String getUsername() { return username; }

    public String getUserID() { return userID; }
}