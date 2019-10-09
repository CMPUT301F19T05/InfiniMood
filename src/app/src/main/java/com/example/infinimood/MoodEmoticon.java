package com.example.infinimood;

public class MoodEmoticon {

    private final String key;

    private final String emoticon;

    // Background color?

    public MoodEmoticon(String key, String emoticon) {
        this.key = key;
        this.emoticon = emoticon;
    }

    public String getKey() {
        return key;
    }

    public String getEmoticon() {
        return emoticon;
    }

}
