package com.example.infinimood.model;

import android.util.Log;

import java.io.Serializable;

/**
 *  User.java
 *  User class
 */

public class User implements Serializable {

    private String userID;
    private String username;

    private boolean currentUserFollows;
    private boolean currentUserRequestedFollow;

    private boolean followsCurrentUser;
    private boolean requestedFollowCurrentUser;


    public User(String Uid, String username) {
        this.userID = Uid;
        this.username = username;
    }

    public User(String userID,
                String username,
                boolean currentUserFollows,
                boolean currentUserRequestedFollow,
                boolean followsCurrentUser,
                boolean requestedFollowCurrentUser)
    {
        this.userID = userID;
        this.username = username;
        this.currentUserFollows = currentUserFollows;
        this.currentUserRequestedFollow = currentUserRequestedFollow;
        this.followsCurrentUser = followsCurrentUser;
        this.requestedFollowCurrentUser = requestedFollowCurrentUser;
    }

    public boolean isCurrentUserFollows() {
        return currentUserFollows;
    }

    public void setCurrentUserFollows(boolean currentUserFollows) {
        this.currentUserFollows = currentUserFollows;
    }

    public boolean isCurrentUserRequestedFollow() {
        return currentUserRequestedFollow;
    }

    public void setCurrentUserRequestedFollow(boolean currentUserRequestedFollow) {
        this.currentUserRequestedFollow = currentUserRequestedFollow;
    }

    public boolean isFollowsCurrentUser() {
        return followsCurrentUser;
    }

    public void setFollowsCurrentUser(boolean followsCurrentUser) {
        this.followsCurrentUser = followsCurrentUser;
    }

    public boolean isRequestedFollowCurrentUser() {
        return requestedFollowCurrentUser;
    }

    public void setRequestedFollowCurrentUser(boolean requestedFollowCurrentUser) {
        this.requestedFollowCurrentUser = requestedFollowCurrentUser;
    }

    public String getUsername() { return username; }

    public String getUserID() { return userID; }

    public void print() {
        Log.i("", "===================================================");
        Log.i("", "ID : " + this.userID);
        Log.i("", "Username : " + this.username);
        Log.i("", "Current User Follows : " + this.currentUserFollows);
        Log.i("", "Current User Requested : " + this.currentUserRequestedFollow);
        Log.i("", "Follows Current User : " + this.followsCurrentUser);
        Log.i("", "Requested Current User : " + this.requestedFollowCurrentUser);
        Log.i("", "===================================================");
    }
}