package com.example.infinimood.model;

import java.util.Comparator;

public class MoodComparator implements Comparator<Mood> {

    // -1 is reversed, 1 is normal
    private int reverse = -1;

    @Override
    public int compare(Mood m1, Mood m2) {
        return reverse * Long.compare(m1.getTime(), m2.getTime());
    }

    public void reverse() {
        reverse = -reverse;
    }
}
