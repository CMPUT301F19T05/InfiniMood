package com.example.infinimood.model;

import android.location.Location;
import java.util.Date;

public class MoodEntry {

    private Date date;
    private Mood emotionalState;
    private String reason;
    private SocialSituation socialSituation;
    private MoodImage image;
    private Location location;

    // temporary constructor for MoodEntry
    public MoodEntry(Date date, Mood emotionalState, String reason, SocialSituation socialSituation) {
        this.date = date;
        this.emotionalState = emotionalState;
        this.reason = reason;
        this.socialSituation = socialSituation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Mood getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(Mood emotionalState) {
        this.emotionalState = emotionalState;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public SocialSituation getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(SocialSituation socialSituation) {
        this.socialSituation = socialSituation;
    }

    public MoodImage getImage() {
        return image;
    }

    public void setImage(MoodImage image) {
        this.image = image;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
// Getters & Setters

}