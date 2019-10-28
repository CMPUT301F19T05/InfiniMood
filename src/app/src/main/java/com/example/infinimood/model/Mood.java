package com.example.infinimood.model;

public class Mood {

    private final MoodEmoticon emoticon;

    private Mood(MoodEmoticon emoticon) {
        this.emoticon = emoticon;
    }

    public String getKey() {
        return emoticon.getKey();
    }
};