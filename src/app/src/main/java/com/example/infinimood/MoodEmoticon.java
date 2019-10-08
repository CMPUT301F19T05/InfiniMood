package com.example.infinimood;

public class MoodEmoticon {

    private final String key;

    private final String emoticion;

    // Background color?

    public MoodEmoticon(String key, String emoticon) {
        this.key = key;
        this.emoticion = emoticon;
    }

    public String getKey() {
        return key;
    }

    public String getEmoticion() {
        return emoticion;
    }

}
