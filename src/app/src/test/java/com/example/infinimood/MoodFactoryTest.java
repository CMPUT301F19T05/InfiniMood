package com.example.infinimood;

import android.location.Location;

import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.AngryMood;
import com.example.infinimood.model.CryingMood;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodFactory;
import com.example.infinimood.model.SocialSituation;

import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class MoodFactoryTest {

    private final static String TEST_ID = "1";
    private final static long TEST_DATE = new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime().getTime();
    private final static String TEST_REASON = "test123";
    private final static Location TEST_LOCATION = null;
    private final static String TEST_SOCIAL_SITUATION = SocialSituation.WITH_CROWD.getDescription();
    private final static boolean TEST_HAS_IMAGE = false;


    private MoodFactory moodFactory = new MoodFactory();

    private void testFactoryByType(String type, Class<? extends Mood> expected) {
        Mood mood = moodFactory.createMood(TEST_ID, type, TEST_DATE, TEST_REASON, TEST_LOCATION, TEST_SOCIAL_SITUATION, TEST_HAS_IMAGE);
        assertEquals(mood.getClass(), expected);
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

}
