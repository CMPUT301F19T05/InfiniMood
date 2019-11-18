package com.example.infinimood.controller;

import com.example.infinimood.model.User;

import java.util.ArrayList;

public interface GetUsersCallback {
    void onCallback(ArrayList<User> usersArrayList);
}
