package com.example.infinimood;

import java.util.ArrayList;

public class UserProfile {

    private ArrayList<MoodEntry> moodHistory = new ArrayList<>();

    public void addMood(MoodEntry entry) {
        moodHistory.add(entry);
    }

}
