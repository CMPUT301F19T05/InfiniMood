package com.example.infinimood;

import com.example.infinimood.model.AfraidMood;
import com.example.infinimood.model.HappyMood;
import com.example.infinimood.model.Mood;
import com.example.infinimood.model.MoodComparator;
import com.example.infinimood.model.SadMood;
import com.example.infinimood.model.SocialSituation;

import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class MoodComparatorTest {

    private Mood afraidAprilMood = new AfraidMood(
            "1",
            new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime(),
            null,
            null,
            SocialSituation.WITH_CROWD.getDescription(),
            null
    );

    private Mood happyAprilMood = new HappyMood(
            "2",
            new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime(),
            null,
            null,
            SocialSituation.WITH_CROWD.getDescription(),
            null
    );

    private Mood sadMayMood = new SadMood(
            "3",
            new GregorianCalendar(2019, GregorianCalendar.MAY, 1).getTime(),
            null,
            null,
            SocialSituation.WITH_CROWD.getDescription(),
            null
    );

    private MoodComparator getNormalComparator() {
        final MoodComparator comparator = new MoodComparator();
        comparator.reverse();
        return comparator;
    }

    private MoodComparator getReverseComparator() {
        return new MoodComparator();
    }

    @Test
    public void testNormalCompare() {
        final MoodComparator normalComparator = getNormalComparator();
        assertEquals(normalComparator.compare(afraidAprilMood, sadMayMood), -1);
        assertEquals(normalComparator.compare(afraidAprilMood, happyAprilMood), 0);
        assertEquals(normalComparator.compare(sadMayMood, happyAprilMood), 1);
    }

    @Test
    public void testReverseCompare() {
        final MoodComparator reverseComparator = getReverseComparator();
        assertEquals(reverseComparator.compare(afraidAprilMood, sadMayMood), 1);
        assertEquals(reverseComparator.compare(afraidAprilMood, happyAprilMood), 0);
        assertEquals(reverseComparator.compare(sadMayMood, happyAprilMood), -1);
    }

}
