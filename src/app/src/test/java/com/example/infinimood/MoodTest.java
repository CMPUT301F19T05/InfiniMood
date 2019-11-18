package com.example.infinimood;

import android.graphics.Bitmap;
import android.location.Location;

import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.AngryMood;
import com.example.infinimood.model.CryingMood;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.InLoveMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.SadMood;
import com.example.infinimood.model.SleepyMood;
import com.example.infinimood.model.SocialSituation;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MoodTest {

    private final static String TEST_ID = "1";
    private final static long TEST_DATE = new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime().getTime();
    private final static String TEST_REASON = "test123";
    private final static Location TEST_LOCATION = null;
    private final static String TEST_SOCIAL_SITUATION = SocialSituation.WITH_CROWD.getDescription();
    private final static Bitmap TEST_IMAGE = null;

    private Mood testConstructor(Class<? extends Mood> moodClass) {
        final Constructor<? extends Mood> constructor;
        final Mood mood;

        try {
            constructor = moodClass.getConstructor(
                    String.class,
                    long.class,
                    String.class,
                    Location.class,
                    String.class,
                    Bitmap.class
            );
        } catch (NoSuchMethodException ex) {
            fail();
            return null;
        }

        try {
            mood = constructor.newInstance(
                    TEST_ID,
                    TEST_DATE,
                    TEST_REASON,
                    TEST_LOCATION,
                    TEST_SOCIAL_SITUATION,
                    TEST_IMAGE
            );
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            fail();
            return null;
        }

        assertEquals(mood.getId(), TEST_ID);
        assertEquals(mood.getDate(), TEST_DATE);
        assertEquals(mood.getReason(), TEST_REASON);
        assertEquals(mood.getLocation(), TEST_LOCATION);
        assertEquals(mood.getSocialSituation(), TEST_SOCIAL_SITUATION);
        assertEquals(mood.getImage(), TEST_IMAGE);

        return mood;
    }

    private void testSetId(Mood mood) {
        final String newId = "2";
        mood.setId(newId);
        assertEquals(mood.getId(), newId);
    }

    private void testSetDate(Mood mood) {
        final long newDate = new GregorianCalendar(2019, GregorianCalendar.MAY, 1).getTime().getTime();
        mood.setDate(newDate);
        assertEquals(mood.getDate(), newDate);
    }

    private void testAll(Class<? extends Mood> moodClass) {
        final Mood mood = testConstructor(moodClass);
        if (mood == null) {
            fail();
        } else {
            testSetId(mood);
            testSetDate(mood);
        }
    }

    @Test
    public void testAfraidMood() {
        testAll(AfraidMood.class);
    }

    @Test
    public void testAngryMood() {
        testAll(AngryMood.class);
    }

    @Test
    public void testCryingMood() {
        testAll(CryingMood.class);
    }

    @Test
    public void testHappyMood() {
        testAll(HappyMood.class);
    }

    @Test
    public void testInLoveMood() {
        testAll(InLoveMood.class);
    }

    @Test
    public void testSadMood() {
        testAll(SadMood.class);
    }

    @Test
    public void testSleepyMood() {
        testAll(SleepyMood.class);
    }

}
