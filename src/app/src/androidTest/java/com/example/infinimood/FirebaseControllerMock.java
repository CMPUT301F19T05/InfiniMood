package com.example.infinimood;

import android.graphics.Bitmap;

import com.example.infinimood.controller.BitmapCallback;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.controller.GetMoodCallback;
import com.example.infinimood.controller.GetMoodsCallback;
import com.example.infinimood.controller.GetUsersCallback;
import com.example.infinimood.controller.StringCallback;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.SocialSituation;
import com.example.infinimood.model.User;
import com.example.infinimood.view.MoodCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.GregorianCalendar;

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

    public ArrayList<User> getUsersResult = new ArrayList<>();
    public int getUsersCallCount = 0;

    @Override
    public void getUsers(GetUsersCallback callback) {
        getUsersCallCount++;
        callback.onCallback(getUsersResult);
    }

    public ArrayList<Mood> refreshOtherUserMoodsResult = new ArrayList<>();
    public int refreshOtherUserMoodsCallCount = 0;

    @Override
    public void refreshOtherUserMoods(User user, GetMoodsCallback callback) {
        refreshOtherUserMoodsCallCount++;
        callback.onCallback(refreshOtherUserMoodsResult);
    }

    public Mood refreshMoodResult = new HappyMood(
            "1",
            "user1",
            new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime().getTime(),
            "",
            null,
            SocialSituation.WITH_CROWD.getDescription(),
            false
    );
    public int refreshMoodCallCount = 0;

    @Override
    public void refreshMood(Mood mood, GetMoodCallback callback) {
        refreshMoodCallCount++;
        callback.onCallback(refreshMoodResult);
    }

    public String getUsernameResult = "Joe Doe";
    public int getUsernameCallCount = 0;

    @Override
    public void getUsername(StringCallback callback) {
        getUsernameCallCount++;
        callback.onCallback(getUsernameResult);
    }

    public Bitmap getProfileImageFromDBResult = null;
    public int getProfileImageFromDBCallCount = 0;

    @Override
    public void getProfileImageFromDB(BitmapCallback callback) {
        getProfileImageFromDBCallCount++;
        callback.onCallback(getProfileImageFromDBResult);
    }

}
