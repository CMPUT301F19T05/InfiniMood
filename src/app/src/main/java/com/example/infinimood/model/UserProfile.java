package com.example.infinimood.model;

import java.util.ArrayList;

public class UserProfile {

    private ArrayList<MoodEntry> moodHistory = new ArrayList<>();

    public void addMood(MoodEntry entry) {
        moodHistory.add(entry);
    }

}
