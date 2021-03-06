package com.example.infinimood;

import android.location.Location;

import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.AngryMood;
import com.example.infinimood.model.CryingMood;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.InLoveMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodFactory;
import com.example.infinimood.model.SadMood;
import com.example.infinimood.model.SleepyMood;
import com.example.infinimood.model.SocialSituation;

import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class MoodFactoryTest {

    private final static String TEST_ID = "1";
    private final static String TEST_USER_ID = "user1";
    private final static long TEST_DATE = new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime().getTime();
    private final static String TEST_REASON = "test123";
    private final static Location TEST_LOCATION = null;
    private final static String TEST_SOCIAL_SITUATION = SocialSituation.WITH_CROWD.getDescription();
    private final static boolean TEST_HAS_IMAGE = false;


    private MoodFactory moodFactory = new MoodFactory();

    private void testFactoryByType(String type, Class<? extends Mood> expected) {
        Mood mood = moodFactory.createMood(TEST_ID, TEST_USER_ID, type, TEST_DATE, TEST_REASON, TEST_LOCATION, TEST_SOCIAL_SITUATION, TEST_HAS_IMAGE);
        assertEquals(expected, mood.getClass());
        assertEquals(TEST_ID, mood.getId());
        assertEquals(TEST_DATE, mood.getDate());
        assertEquals(TEST_REASON, mood.getReason());
        assertEquals(TEST_LOCATION, mood.getLocation());
        assertEquals(TEST_SOCIAL_SITUATION, mood.getSocialSituation());
        assertEquals(TEST_HAS_IMAGE, mood.hasImage());
    }

    @Test
    public void testFactoryAfraidMood() {
        testFactoryByType("Afraid", AfraidMood.class);
    }

    @Test
    public void testFactoryAngryMood() {
        testFactoryByType("Angry", AngryMood.class);
    }

    @Test
    public void testFactoryCryingMood() {
        testFactoryByType("Crying", CryingMood.class);
    }

    @Test
    public void testFactoryHappyMood() {
        testFactoryByType("Happy", HappyMood.class);
    }

    @Test
    public void testFactoryInLoveMood() {
        testFactoryByType("In Love", InLoveMood.class);
    }

    @Test
    public void testFactorySadMood() {
        testFactoryByType("Sad", SadMood.class);
    }

    @Test
    public void testFactorySleepyMood() {
        testFactoryByType("Sleepy", SleepyMood.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryInvalidMood() {
        testFactoryByType("Invalid", null);
    }

}
