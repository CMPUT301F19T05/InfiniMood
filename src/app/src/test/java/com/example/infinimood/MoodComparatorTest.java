package com.example.infinimood;

import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.SocialSituation;

import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class MoodComparatorTest {

    private MoodComparator comparator = new MoodComparator();

    private Mood mood1 = new AfraidMood(
            "1",
            new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime(),
            null,
            null,
            SocialSituation.WITH_CROWD.getDescription(),
            null
    );

    private Mood mood2 = new AfraidMood(
            "1",
            new GregorianCalendar(2019, GregorianCalendar.MAY, 1).getTime(),
            null,
            null,
            SocialSituation.WITH_CROWD.getDescription(),
            null
    );

    @Test
    public void testCompare() {
        assertEquals(comparator.compare(mood1, mood2), 1);
    }

}
