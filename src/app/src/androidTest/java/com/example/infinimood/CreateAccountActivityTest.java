package com.example.infinimood;

import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.infinimood.view.CreateAccountActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class CreateAccountActivityTest {

    @Rule
    public ActivityTestRule<CreateAccountActivity> rule = new ActivityTestRule<>(
            CreateAccountActivity.class,
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

    private void fillForm(String username, String email, String password, String passwordRepeat) {
        if (username != null) {
            solo.enterText((EditText)solo.getView(R.id.createAccountUsernameEditText), username);
        }
        if (email != null) {
            solo.enterText((EditText)solo.getView(R.id.loginCreateAccountEmailEditText), email);
        }
        if (password != null) {
            solo.enterText((EditText)solo.getView(R.id.loginCreateAccountPasswordEditText), password);
        }
        if (passwordRepeat != null) {
            solo.enterText((EditText)solo.getView(R.id.createAccountPasswordRepeatEditText), passwordRepeat);
        }
    }

    @Test
    public void testUsernameValidation() {
        solo.assertCurrentActivity("Expected login screen to show", CreateAccountActivity.class);
        fillForm(null, "test@example.com", "123456", "123456");
        solo.clickOnButton(solo.getString(R.string.submit));
        assertTrue(solo.waitForText(solo.getString(R.string.user)));
    }

}
