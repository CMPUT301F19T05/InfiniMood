package com.example.infinimood.model;

public enum Mood {
    HAPPY(new MoodEmoticon("happy", "TODO")),
    ANGRY(new MoodEmoticon("angry", "TODO")),
    FURIOUS(new MoodEmoticon("furious", "TODO")),
    LONELY(new MoodEmoticon("lonely", "TODO")),
    FEARFUL(new MoodEmoticon("fearful", "TODO"));

    private final MoodEmoticon emoticon;

    private Mood(MoodEmoticon emoticon) {
        this.emoticon = emoticon;
    }

    public String getKey() {
        return emoticon.getKey();
    }
};