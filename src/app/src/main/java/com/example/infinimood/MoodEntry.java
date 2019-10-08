package com.example.infinimood;

import java.util.Date;

public class MoodEntry {

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

    public enum SocialSituation {
        ALONE("alone"),
        WITH_ONE("with one other person"),
        WITH_SEVERAL("with two to several people"),
        WITH_CROWD("with a crowd");

        private final String description;

        private SocialSituation(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private Date date;

    private Mood emotionalState;

    private String reason;

    private SocialSituation socialSituation;

    private MoodImage image;

    // Location?

    private boolean isDirty = false;

    // Getters & Setters

}
