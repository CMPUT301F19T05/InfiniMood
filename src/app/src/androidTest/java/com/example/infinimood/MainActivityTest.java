package com.example.infinimood;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.infinimood.view.CreateAccountActivity;
import com.example.infinimood.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(
            MainActivity.class,
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
        solo.assertCurrentActivity("Expected login screen to show", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.loginSignUpTextView));
        solo.assertCurrentActivity("Expected create account screen to show", CreateAccountActivity.class);
        solo.clickOnButton(solo.getString(R.string.back));
        solo.assertCurrentActivity("Expected login screen to show", MainActivity.class);
    }

    @Test
    public void testTravis() {
        fail();
    }

}
