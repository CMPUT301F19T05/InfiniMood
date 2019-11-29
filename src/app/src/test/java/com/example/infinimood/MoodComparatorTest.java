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
            "user1",
            new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime().getTime(),
            "",
            null,
            SocialSituation.WITH_CROWD.getDescription(),
            false
    );

    private Mood happyAprilMood = new HappyMood(
            "2",
            "user1",
            new GregorianCalendar(2019, GregorianCalendar.APRIL, 1).getTime().getTime(),
            "",
            null,
            SocialSituation.WITH_CROWD.getDescription(),
            false
    );

    private Mood sadMayMood = new SadMood(
            "3",
            "user1",
            new GregorianCalendar(2019, GregorianCalendar.MAY, 1).getTime().getTime(),
            "",
            null,
            SocialSituation.WITH_CROWD.getDescription(),
            false
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
        assertEquals(-1, normalComparator.compare(afraidAprilMood, sadMayMood), 0);
        assertEquals(0, normalComparator.compare(afraidAprilMood, happyAprilMood), 0);
        assertEquals(1, normalComparator.compare(sadMayMood, happyAprilMood), 0);
    }

    @Test
    public void testReverseCompare() {
        final MoodComparator reverseComparator = getReverseComparator();
        assertEquals(1, reverseComparator.compare(afraidAprilMood, sadMayMood), 0);
        assertEquals(0, reverseComparator.compare(afraidAprilMood, happyAprilMood), 0);
        assertEquals(-1, reverseComparator.compare(sadMayMood, happyAprilMood), 0);
    }

}
