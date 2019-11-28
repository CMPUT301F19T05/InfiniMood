package com.example.infinimood;

import android.graphics.Bitmap;

import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.controller.GetMoodCallback;
import com.example.infinimood.controller.GetUsersCallback;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.User;
import com.example.infinimood.view.MoodCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class FirebaseControllerMock extends FirebaseController {
    public static FirebaseControllerMock install() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final FirebaseControllerMock firebaseControllerMock = new FirebaseControllerMock();
        final Method setFirebaseController = MoodCompatActivity.class.getDeclaredMethod("setFirebaseController", FirebaseController.class);
        setFirebaseController.setAccessible(true);
        setFirebaseController.invoke(null, firebaseControllerMock);
        return firebaseControllerMock;
    }

    public boolean userAuthenticatedResult = true;
    public int userAuthenticatedCallCount = 0;

    @Override
    public boolean userAuthenticated() {
        userAuthenticatedCallCount++;
        return userAuthenticatedResult;
    }

    public String getCurrentUIDResult = "user1";
    public int getCurrentUIDCallCount = 0;

    @Override
    public String getCurrentUID() {
        getCurrentUIDCallCount++;
        return getCurrentUIDResult;
    }

    public boolean addImageToDBResult = true;
    public int addImageToDbCallCount = 0;
    public ArrayList<Mood> addImageToDBCallArgsMood = new ArrayList<>();
    public ArrayList<Bitmap> addImageToDBCallArgsBitmap = new ArrayList<>();

    @Override
    public void uploadMoodImageToDB(Mood mood, Bitmap bitmap, BooleanCallback callback) {
        addImageToDbCallCount++;
        addImageToDBCallArgsMood.add(mood);
        addImageToDBCallArgsBitmap.add(bitmap);
        callback.onCallback(addImageToDBResult);
    }

    public boolean addMoodEventToDBResult = true;
    public int addMoodEventToDBCallCount = 0;
    public ArrayList<Mood> addMoodEventToDbCallArgsMood = new ArrayList<>();

    @Override
    public void addMoodEventToDB(Mood mood, BooleanCallback callback) {
        addMoodEventToDBCallCount++;
        addMoodEventToDbCallArgsMood.add(mood);
        callback.onCallback(addMoodEventToDBResult);
    }

    public ArrayList<User> getUsersResult = new ArrayList<User>();
    public int getUsersResultCallCount = 0;

    @Override
    public void getUsers(GetUsersCallback callback) {
        callback.onCallback(getUsersResult);
    }

    public ArrayList<User> refreshOtherUserMoods

    @Override
    public void refreshOtherUserMoods(User user, GetMoodCallback callback) {

    }
}
