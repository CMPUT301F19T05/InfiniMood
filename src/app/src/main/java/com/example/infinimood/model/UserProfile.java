package com.example.infinimood.model;

import java.util.ArrayList;

public class UserProfile {

    private ArrayList<Mood> moodHistory = new ArrayList<>();

    public void addMood(Mood entry) {
        moodHistory.add(entry);
    }

}
