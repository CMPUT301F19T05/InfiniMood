package com.example.infinimood.model;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Date;

/**
 * Mood.java
 * Mood event superclass, contains all functionality for mood objects
 */
public class Mood implements Parcelable {

    private String id;
    private long dateTimestamp;
    private String reason = "";
    private Location location = null;
    private String socialSituation;
    private boolean hasImage = false;

    private String mood = "";
    private String icon = "";
    private String color;

    public Mood(String id,
                long dateTimestamp,
                String reason,
                Location location,
                String socialSituation,
                boolean hasImage) {
        this.id = id;
        this.dateTimestamp = dateTimestamp;
        this.reason = reason;
        this.location = location;
        this.socialSituation = socialSituation;
        this.hasImage = hasImage;
    }

    public Mood(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(dateTimestamp);
        dest.writeString(reason);

        if (location != null) {
            dest.writeString(String.valueOf(true));
            dest.writeDouble(location.getLatitude());
            dest.writeDouble(location.getLongitude());
        } else {
            dest.writeString(String.valueOf(false));
            dest.writeDouble(0);
            dest.writeDouble(0);
        }

        dest.writeString(socialSituation);
        dest.writeString(String.valueOf(hasImage));
        dest.writeString(mood);
        dest.writeString(icon);
        dest.writeString(color);
    }

    private void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.dateTimestamp = in.readLong();
        this.reason = in.readString();

        boolean hasLocation = Boolean.valueOf(in.readString());
        if (hasLocation) {
            this.location = new Location("");
            this.location.setLatitude(in.readDouble());
            this.location.setLongitude(in.readDouble());
        } else {
            this.location = null;
            in.readDouble();
            in.readDouble();
        }

        this.socialSituation = in.readString();
        this.hasImage = Boolean.valueOf(in.readString());

        this.mood = in.readString();
        this.icon = in.readString();
        this.color = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Mood createFromParcel(Parcel in) {
                    return new Mood(in);
                }

                public Mood[] newArray(int size) {
                    return new Mood[size];
                }
            };


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return dateTimestamp;
    }

    public void setDate(long dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
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

    public String getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
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

    public void print() {
        Log.i("", "ID : " + this.id);
        Log.i("", "Mood : " + this.mood);
        Log.i("", "Social Situation : " + this.socialSituation);
        Log.i("", "Reason : " + this.reason);
        Log.i("", "Date : " + new Date(this.dateTimestamp).toString());
        Log.i("", "Emoji : " + this.icon);
    }
}
