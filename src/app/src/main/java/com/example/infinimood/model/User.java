package com.example.infinimood.model;

/**
 *  User.java
 *  User class
 */

public class User {

    private String userID;
    private String username;

    private boolean current_user_follows;
    private boolean current_user_requested_follow;

    private boolean follows_current_user;
    private boolean requested_follow_current_user;


    public User(String Uid, String username) {
        this.userID = Uid;
        this.username = username;
    }

    public boolean isCurrent_user_follows() {
        return current_user_follows;
    }

    public void setCurrent_user_follows(boolean current_user_follows) {
        this.current_user_follows = current_user_follows;
    }

    public boolean isCurrent_user_requested_follow() {
        return current_user_requested_follow;
    }

    public void setCurrent_user_requested_follow(boolean current_user_requested_follow) {
        this.current_user_requested_follow = current_user_requested_follow;
    }

    public boolean isFollows_current_user() {
        return follows_current_user;
    }

    public void setFollows_current_user(boolean follows_current_user) {
        this.follows_current_user = follows_current_user;
    }

    public boolean isRequested_follow_current_user() {
        return requested_follow_current_user;
    }

    public void setRequested_follow_current_user(boolean requested_follow_current_user) {
        this.requested_follow_current_user = requested_follow_current_user;
    }

    public String getUsername() { return username; }

    public String getUserID() { return userID; }
}