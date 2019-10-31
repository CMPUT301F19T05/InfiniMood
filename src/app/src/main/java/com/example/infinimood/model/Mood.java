package com.example.infinimood.model;

import android.graphics.Color;
import android.location.Location;
import android.media.Image;

import java.util.Date;

public abstract class Mood {

    private String id;
    private Date date;
    private String reason = "";
    private Location location = null;
    private String social_situation;
    private Image image = null;

    private String mood = "";
    private String icon = "";
    private String color;

    public Mood() {}

    public Mood(String id,
                Date date,
                String reason,
                Location location,
                String social_situation,
                Image image)
    {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.location = location;
        this.social_situation = social_situation;
        this.image = image;
    }

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getSocial_situation() {
        return social_situation;
    }

    public void setSocial_situation(String social_situation) {
        this.social_situation = social_situation;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    //TODO: Implement these function once Firebase is working
    public void commitToDatabase() {

    }
}