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
public class AddEditMoodActivityTest {

    private FirebaseControllerMock mockController;

    public AddEditMoodActivityTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        mockController = FirebaseControllerMock.install();
    }

    @Rule
    public ActivityTestRule<AddEditMoodActivity> rule = new ActivityTestRule<>(
            AddEditMoodActivity.class,
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
        solo.assertCurrentActivity("Expected add edit mode screen to show", AddEditMoodActivity.class);
        solo.clickOnButton(solo.getString(R.string.submit));
        solo.waitForText(solo.getString(R.string.add_mood_successfully_saved));
        final Mood mood = mockController.addMoodEventToDbCallArgsMood.get(0);
        assertEquals(0, mockController.addImageToDbCallCount, 0);
        assertEquals(1, mockController.addMoodEventToDBCallCount, 0);
        assertEquals("Happy", mood.getMood());
        assertEquals("None", mood.getSocialSituation());
        assertEquals("", mood.getReason());
        assertNull(mood.getLocation());
        assertFalse(mood.hasImage());
    }

    @Test
    public void testAddComplexMood() {
        solo.assertCurrentActivity("Expected add edit mode screen to show", AddEditMoodActivity.class);

        final String moodType = "Angry";
        solo.clickOnView(solo.getView(R.id.addEditMoodSpinner));
        solo.clickOnText(moodType);

        final String socialSituation = "With a crowd";
        solo.clickOnView(solo.getView(R.id.addEditSocialSituationSpinner));
        solo.clickOnText(socialSituation);

        final String reason = "Just angry";
        solo.enterText((EditText) solo.getView(R.id.addEditReasonEditText), reason);

        solo.clickOnButton(solo.getString(R.string.submit));
        solo.waitForText("Successfully added Mood event");

        final Mood mood = mockController.addMoodEventToDbCallArgsMood.get(0);
        assertEquals(0, mockController.addImageToDbCallCount, 0);
        assertEquals(1, mockController.addMoodEventToDBCallCount, 0);
        assertEquals(moodType, mood.getMood());
        assertEquals(socialSituation, mood.getSocialSituation());
        assertEquals(reason, mood.getReason());
        assertNull(mood.getLocation());
        assertFalse(mood.hasImage());
    }

}
