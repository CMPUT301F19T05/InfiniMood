package com.example.infinimood;

import android.graphics.Bitmap;

import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.model.Mood;
import com.example.infinimood.view.MoodCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class FirebaseControllerMock extends FirebaseController {
    public static FirebaseControllerMock install() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final FirebaseControllerMock firebaseControllerMock = new FirebaseControllerMock();
        final Method setFirebaseController = MoodCompatActivity.class.getDeclaredMethod("setFirebaseController", FirebaseController.class);
        setFirebaseController.setAccessible(true);
        setFirebaseController.invoke(null, firebaseControllerMock);
        return firebaseControllerMock;
    }

    public boolean userAuthenticatedResult = true;

    @Override
    public boolean userAuthenticated() {
        return userAuthenticatedResult;
    }

    public String getCurrentUIDResult = "user1";

    @Override
    public String getCurrentUID() {
        return getCurrentUIDResult;
    }

    public boolean addImageToDBResult = true;

    @Override
    public void addImageToDB(Mood mood, Bitmap bitmap, BooleanCallback callback) {
        callback.onCallback(addImageToDBResult);
    }

    public boolean addMoodEventToDBResult = true;

    @Override
    public void addMoodEventToDB(Mood mood, BooleanCallback callback) {
        callback.onCallback(addMoodEventToDBResult);
    }
}
