package com.example.infinimood.model;

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