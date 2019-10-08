package com.example.infinimood;

import java.util.Date;

public class MoodEntry {

    public enum Mood {
        HAPPY(new MoodEmoticon("happy", "TODO")),
        ANGRY(new MoodEmoticon("angry", "TODO")),
        FURIOUS(new MoodEmoticon("furious", "TODO")),
        LONELY(new MoodEmoticon("lonely", "TODO")),
        FEARFUL(new MoodEmoticon("fearful", "TODO"));

        private MoodEmoticon emoticon;

        private Mood(MoodEmoticon emoticon) {
            this.emoticon = emoticon;
        }

        public String getKey() {
            return emoticon.getKey();
        }
    };

    private Date date;

    private Mood emotionalState;

    private String reason;

    private String socialSituation;

    private boolean isDirty = false;

    // Getters & Setters

}
