package com.example.infinimood;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.infinimood.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public void startLoginActivity() {
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

}
