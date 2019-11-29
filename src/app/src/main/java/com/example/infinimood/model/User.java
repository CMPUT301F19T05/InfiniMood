package com.example.infinimood.model;

import java.io.Serializable;

/**
 * User.java
 * User class
 */
public class User implements Serializable {

    private String userID;
    private String username;

    private boolean currentUserFollows;
    private boolean currentUserRequestedFollow;

    private boolean followsCurrentUser;
    private boolean requestedFollowCurrentUser;

    /**
     * User
     * Simple constructor for User
     *
     * @param Uid      String - Unique id for user
     * @param username String - Unique Username for User
     */
    public User(String Uid, String username) {
        this.userID = Uid;
        this.username = username;
    }

    /**
     * User
     * Simple constructor for User
     *
     * @param userID                     String - Unique ID for user
     * @param username                   String - Unique username for user
     * @param currentUserFollows         boolean - whether the currently logged in user follows this user
     * @param currentUserRequestedFollow boolean - whether the currently logged i user has requested
     *                                   to follow this user
     * @param followsCurrentUser         boolean - whether this user follows the currently logged in user
     * @param requestedFollowCurrentUser boolean - whether this user has requested a follow from
     *                                   the currently logged in user
     */
    public User(String userID,
                String username,
                boolean currentUserFollows,
                boolean currentUserRequestedFollow,
                boolean followsCurrentUser,
                boolean requestedFollowCurrentUser) {
        this.userID = userID;
        this.username = username;
        this.currentUserFollows = currentUserFollows;
        this.currentUserRequestedFollow = currentUserRequestedFollow;
        this.followsCurrentUser = followsCurrentUser;
        this.requestedFollowCurrentUser = requestedFollowCurrentUser;
    }

    /**
     * isCurrentUserFollows
     *
     * @return boolean - whether current user follows this user
     */
    public boolean isCurrentUserFollows() {
        return currentUserFollows;
    }

    /**
     * setCurrentUserFollows
     *
     * @param currentUserFollows boolean - whether current user follows this user
     */
    public void setCurrentUserFollows(boolean currentUserFollows) {
        this.currentUserFollows = currentUserFollows;
    }

    /**
     * isCurrentUserRequestedFollow
     *
     * @return boolean - whether current user is requesting to follow this user
     */
    public boolean isCurrentUserRequestedFollow() {
        return currentUserRequestedFollow;
    }

    /**
     * setCurrentUserRequestedFollow
     *
     * @param currentUserRequestedFollow boolean - whether current user has requested to follow this
     *                                   user
     */
    public void setCurrentUserRequestedFollow(boolean currentUserRequestedFollow) {
        this.currentUserRequestedFollow = currentUserRequestedFollow;
    }

    /**
     * isFollowCurrentUser
     *
     * @return boolean - whether this user follows the current user
     */
    public boolean isFollowsCurrentUser() {
        return followsCurrentUser;
    }

    /**
     * setFollowsCurrentUser
     *
     * @param followsCurrentUser boolean - whether this user follwos the current user
     */
    public void setFollowsCurrentUser(boolean followsCurrentUser) {
        this.followsCurrentUser = followsCurrentUser;
    }

    /**
     * isRequestedFollowCurrentUser
     *
     * @return boolean - whether this user has requested to follow current user
     */
    public boolean isRequestedFollowCurrentUser() {
        return requestedFollowCurrentUser;
    }

    /**
     * setRequestedFollowCurrentUser
     *
     * @param requestedFollowCurrentUser boolean - whether this user has requested to follow current
     *                                   user
     */
    public void setRequestedFollowCurrentUser(boolean requestedFollowCurrentUser) {
        this.requestedFollowCurrentUser = requestedFollowCurrentUser;
    }

    /**
     * getUsername
     *
     * @return String - user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * getUserID
     *
     * @return String - user's ID
     */
    public String getUserID() {
        return userID;
    }

}
