package com.example.infinimood;

import android.location.Location;
import android.media.Image;

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
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MoodTest {

    private final static String TEST_ID = "1";
    private final static Date TEST_DATE = new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime();
    private final static String TEST_REASON = "test123";
    private final static Location TEST_LOCATION = null;
    private final static String TEST_SOCIAL_SITUATION = SocialSituation.WITH_CROWD.getDescription();
    private final static Image TEST_IMAGE = null;

    private void testConstructor(Class<? extends Mood> moodClass) {
        final Constructor<?> constructor;
        final Mood mood;

        try {
            constructor = moodClass.getConstructor(
                    String.class,
                    Date.class,
                    String.class,
                    Location.class,
                    String.class,
                    Image.class
            );
        } catch (NoSuchMethodException ex) {
            fail();
            return;
        }

        try {
            mood = (Mood) constructor.newInstance(
                    TEST_ID,
                    TEST_DATE,
                    TEST_REASON,
                    TEST_LOCATION,
                    TEST_SOCIAL_SITUATION,
                    TEST_IMAGE
            );
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            fail();
            return;
        }

        assertEquals(mood.getId(), TEST_ID);
        assertEquals(mood.getDate(), TEST_DATE);
        assertEquals(mood.getReason(), TEST_REASON);
        assertEquals(mood.getLocation(), TEST_LOCATION);
        assertEquals(mood.getSocialSituation(), TEST_SOCIAL_SITUATION);
        assertEquals(mood.getImage(), TEST_IMAGE);
    }

    @Test
    public void testAfraidMood() {
        testConstructor(AfraidMood.class);
    }

    @Test
    public void testAngryMood() {
        testConstructor(AngryMood.class);
    }

    @Test
    public void testCryingMood() {
        testConstructor(CryingMood.class);
    }

    @Test
    public void testHappyMood() {
        testConstructor(HappyMood.class);
    }

    @Test
    public void testInLoveMood() {
        testConstructor(InLoveMood.class);
    }

    @Test
    public void testSadMood() {
        testConstructor(SadMood.class);
    }

    @Test
    public void testSleepyMood() {
        testConstructor(SleepyMood.class);
    }

}
