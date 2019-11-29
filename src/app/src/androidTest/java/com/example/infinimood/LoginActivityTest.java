package com.example.infinimood;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.infinimood.view.CreateAccountActivity;
import com.example.infinimood.view.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    public LoginActivityTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final FirebaseControllerMock mockController = FirebaseControllerMock.install();
        mockController.userAuthenticatedResult = false;
    }

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(
            LoginActivity.class,
            true,
            true
    );

    private Solo solo;

    private FirebaseAuth auth;

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        auth = FirebaseAuth.getInstance();
        auth.signOut();
    }

    @After
    public void tearDown() {
        auth.signOut();
        solo.finishOpenedActivities();
    }

    @Test
    public void testStartCreateAccountActivity() {
        solo.assertCurrentActivity("Expected login screen to show", LoginActivity.class);
        solo.clickOnView(solo.getView(R.id.loginSignUpTextView));
        solo.assertCurrentActivity("Expected create account screen to show", CreateAccountActivity.class);
        solo.clickOnButton(solo.getString(R.string.back));
        solo.assertCurrentActivity("Expected login screen to show", LoginActivity.class);
    }

}
