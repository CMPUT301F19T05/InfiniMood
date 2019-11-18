package com.example.infinimood.controller;

import android.content.Intent;

import com.example.infinimood.view.CreateAccountActivity;
import com.example.infinimood.view.MoodCompatActivity;
import com.example.infinimood.view.UserProfileActivity;

public class MainController {

    private MoodCompatActivity activity;

    public MainController(MoodCompatActivity activity) {
        this.activity = activity;
    }

    public void userLoggedIn(){
        activity.startActivityNoHistory(UserProfileActivity.class);
    }

    public void logIn() {
        // TODO
    }

    public void signUp() {
        final Intent intent = new Intent(activity, CreateAccountActivity.class);
        activity.startActivity(intent);
    }

}
