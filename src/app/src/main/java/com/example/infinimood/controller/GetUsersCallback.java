package com.example.infinimood.controller;

import com.example.infinimood.model.User;

import java.util.ArrayList;

/**
 * GetUsersCallback
 * An interface for users callbacks, to be implemented by any methods needing a callback with
 * a list of Users.
 */
public interface GetUsersCallback {
    void onCallback(ArrayList<User> usersArrayList);
}
