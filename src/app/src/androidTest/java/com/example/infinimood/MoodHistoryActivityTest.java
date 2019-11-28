package com.example.infinimood;

import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.infinimood.model.Mood;
import com.example.infinimood.view.AddEditMoodActivity;
import com.example.infinimood.view.MoodHistoryActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class MoodHistoryActivityTest {

    private FirebaseControllerMock mockController;

    public MoodHistoryActivityTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        mockController = FirebaseControllerMock.install();
    }

    @Rule
    public ActivityTestRule<MoodHistoryActivity> rule = new ActivityTestRule<>(
            MoodHistoryActivity.class,
            true,
            true
    );

    private Solo solo;

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @After
    public void tearDown() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        solo.finishOpenedActivities();
        mockController = FirebaseControllerMock.install();
    }

    @Test
    public void testAddSimpleMoodEvent() {
        solo.assertCurrentActivity("Expected add edit mode screen to show", MoodHistoryActivity.class);
        solo.sleep(9001);
    }

}
