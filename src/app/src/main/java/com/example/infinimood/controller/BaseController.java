package com.example.infinimood.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * BaseController.java
 * Superclass for all other controllers
 * Contains common functionality for all controllers
 */

public abstract class BaseController {

    protected FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

}
