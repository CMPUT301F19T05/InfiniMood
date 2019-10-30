package com.example.infinimood.model;

import android.graphics.Color;
import android.location.Location;
import android.media.Image;

import java.util.Date;

public abstract class Mood {

    private String id;
    private Date date;
    private String reason;
    private Location location;
    private String social_situation;
    private String moodString;
    private Image image;

    public Mood(String id)
    {
        this.loadFromDatabase(id);
    }

    public Mood(String id,
                Date date,
                String reason,
                Location location,
                Image image)
    {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.location = location;
        this.image = image;
    }

    public Mood(String id,
                Date date,
                String reason,
                String social_situation)
    {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.social_situation = social_situation;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // For the mood subclasses to implement depending on the mood
    public abstract String getMoodIcon();
    public abstract int getMoodColor();

    //TODO: Implement these function once Firebase is working
    public void commitToDatabase() {

    }

    public void loadFromDatabase(String id) {

    }
}