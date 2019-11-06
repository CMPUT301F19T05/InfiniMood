package com.example.infinimood.model;

/**
 *  SocialSituation.java
 *  Enum containing different social situation strings for Mood events
 */

public enum SocialSituation {
    ALONE("Alone"),
    WITH_ONE("With one other person"),
    WITH_SEVERAL("With two to several people"),
    WITH_CROWD("With a crowd");

    private final String description;

    private SocialSituation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}