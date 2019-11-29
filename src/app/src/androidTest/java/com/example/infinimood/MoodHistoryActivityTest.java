package com.example.infinimood;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.infinimood.model.AngryMood;
import com.example.infinimood.model.InLoveMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.SocialSituation;
import com.example.infinimood.model.User;
import com.example.infinimood.view.MoodHistoryActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MoodHistoryActivityTest {

    private FirebaseControllerMock mockController;

    private void installControllerMock() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        mockController = FirebaseControllerMock.install();

        final ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1", "joe", false, false, false, false));
        users.add(new User("user2", "doe", false, false, false, false));
        mockController.getUsersResult = users;

        final ArrayList<Mood> moods = new ArrayList<>();
        moods.add(new AngryMood(
                "1",
                "user1",
                new GregorianCalendar(2019, GregorianCalendar.APRIL, 5).getTime().getTime(),
                "",
                null,
                SocialSituation.WITH_CROWD.getDescription(),
                false
        ));
        moods.add(new InLoveMood(
                "2",
                "user1",
                new GregorianCalendar(2019, GregorianCalendar.MAY, 15).getTime().getTime(),
                "<3",
                null,
                SocialSituation.WITH_ONE.getDescription(),
                false
        ));
        mockController.refreshOtherUserMoodsResult = moods;
    }

    public MoodHistoryActivityTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        installControllerMock();
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
        installControllerMock();
    }

    @Test
    public void testViewMoodHistory() {
        solo.assertCurrentActivity("Expected mood history screen to show", MoodHistoryActivity.class);
        assertTrue(solo.waitForText("In Love"));
        assertTrue(solo.waitForText("May 15 2019 12:00 AM"));
        assertTrue(solo.waitForText("<3"));
        assertTrue(solo.waitForText("Angry"));
        assertTrue(solo.waitForText("April 5 2019 12:00 AM"));
    }

    @Test
    public void testExpandMoodHistory() {
        solo.assertCurrentActivity("Expected mood history screen to show", MoodHistoryActivity.class);
        solo.clickOnText("May 15 2019 12:00 AM");
        assertTrue(solo.waitForText("With one other person"));
    }

}
