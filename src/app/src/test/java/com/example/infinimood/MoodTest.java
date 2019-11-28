package com.example.infinimood;

import android.location.Location;

import com.example.infinimood.model.Mood;
import com.example.infinimood.model.SocialSituation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MoodTest {

    private final static String TEST_ID = "1";
    private final static String TEST_USER_ID = "user1";
    private final static long TEST_DATE = new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime().getTime();
    private final static String TEST_REASON = "test123";
    private final static Location TEST_LOCATION = null;
    private final static String TEST_SOCIAL_SITUATION = SocialSituation.WITH_CROWD.getDescription();
    private final static boolean TEST_HAS_IMAGE = false;

    private Mood testConstructor(Class<? extends Mood> moodClass) {
        final Constructor<? extends Mood> constructor;
        final Mood mood;

        try {
            constructor = moodClass.getConstructor(
                    String.class,
                    String.class,
                    long.class,
                    String.class,
                    Location.class,
                    String.class,
                    boolean.class
            );
        } catch (NoSuchMethodException ex) {
            fail();
            return null;
        }

        try {
            mood = constructor.newInstance(
                    TEST_ID,
                    TEST_USER_ID,
                    TEST_DATE,
                    TEST_REASON,
                    TEST_LOCATION,
                    TEST_SOCIAL_SITUATION,
                    TEST_HAS_IMAGE
            );
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            fail();
            return null;
        }

        assertEquals(TEST_ID, mood.getId());
        assertEquals(TEST_DATE, mood.getDate());
        assertEquals(TEST_REASON, mood.getReason());
        assertEquals(TEST_LOCATION, mood.getLocation());
        assertEquals(TEST_SOCIAL_SITUATION, mood.getSocialSituation());
        assertEquals(TEST_HAS_IMAGE, mood.hasImage());

        return mood;
    }

    private void testSetId(Mood mood) {
        final String newId = "2";
        mood.setId(newId);
        assertEquals(newId, mood.getId());
    }

    private void testSetUserId(Mood mood) {
        final String newUserId = "user2";
        mood.setUserId(newUserId);
        assertEquals(newUserId, mood.getUserId());
    }

    private void testSetDate(Mood mood) {
        final long newDate = new GregorianCalendar(2019, GregorianCalendar.MAY, 1).getTime().getTime();
        mood.setDate(newDate);
        assertEquals(newDate, mood.getDate());
    }

    private void testSetReason(Mood mood) {
        final String newReason = "test 321";
        mood.setReason(newReason);
        assertEquals(newReason, mood.getReason());
    }

    private void testSetLocation(Mood mood) {
        final Location newLocation = new Location("");
        mood.setLocation(newLocation);
        assertEquals(newLocation, mood.getLocation());
    }

    private void testSetSocialSituation(Mood mood) {
        final String newSituation = SocialSituation.WITH_CROWD.getDescription();
        mood.setSocialSituation(newSituation);
        assertEquals(newSituation, mood.getSocialSituation());
    }

    private void testSetHasImage(Mood mood) {
        mood.setHasImage(true);
        assertTrue(mood.hasImage());
    }

    protected void testAll(Class<? extends Mood> moodClass) {
        final Mood mood = testConstructor(moodClass);
        if (mood == null) {
            fail();
        } else {
            testSetId(mood);
            testSetUserId(mood);
            testSetDate(mood);
            testSetReason(mood);
            testSetLocation(mood);
            testSetSocialSituation(mood);
            testSetHasImage(mood);
        }
    }

}
