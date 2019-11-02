package com.example.infinimood.model;

import java.util.Comparator;

public class MoodComparator implements Comparator<Mood> {
    @Override
    public int compare(Mood m1, Mood m2) {
        return -Long.compare(m1.getTime(), m2.getTime());
    }
}
