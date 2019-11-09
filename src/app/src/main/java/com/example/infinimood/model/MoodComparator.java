package com.example.infinimood.model;

import java.util.Comparator;

/**
 * MoodComparator.java
 * Comparator used for sorting lists of Mood objects.
 * Sorts in chronological order (based on mood.date)
 */

public class MoodComparator implements Comparator<Mood> {

    // -1 is reversed, 1 is normal
    private int reverse = -1;

    @Override
    public int compare(Mood a, Mood b) {
        return reverse * Long.compare(a.getTime(), b.getTime());
    }

    public void reverse() {
        reverse = -reverse;
    }

}
