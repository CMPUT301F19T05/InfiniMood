package com.example.infinimood.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *
 */
public abstract class BaseController {

    protected FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

}
