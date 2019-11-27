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

    /**
     * Simple constructor for Mood
     * @param id String - unique ID for the mood
     * @param dateTimestamp long - serialized date and time
     * @param reason String - reason for the mood
     * @param location Location - location of the mood
     * @param socialSituation String - social situation when the mood took place
     * @param hasImage boolean - indicating whether image exists for this mood
     */
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

    /**
     * Mood
     * Simple constructor for Mood
     * @param in Parcel - The serialized Parcel representing the mood
     */
    public Mood(Parcel in) {
        readFromParcel(in);
    }

    /**
     * describeContents
     * Overrides describeContents
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * writeToParcel
     * Overrides writeToParcel. Writes the mood event to a given parcel destination
     * @param dest Parcel - The desired destination for the parcel to be written to
     * @param flags int - Required input param, not used
     */
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

    /**
     * readFromParcel
     * Sets mood data from an input Parcel
     * @param in Parcel - the input Parcel
     */
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

    /**
     * Mood creator object that produces Moods from Parcels
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Mood createFromParcel(Parcel in) {
                    return new Mood(in);
                }

                public Mood[] newArray(int size) {
                    return new Mood[size];
                }
            };


    /**
     * getId
     * @return String - id
     */
    public String getId() {
        return id;
    }

    /**
     * setId
     * @param id String - id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getDate
     * @return long - date
     */
    public long getDate() {
        return dateTimestamp;
    }

    /**
     * setDate
     * @param dateTimestamp long - date and time to set
     */
    public void setDate(long dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    /**
     * getReason
     * @return String - reason for the mood
     */
    public String getReason() {
        return reason;
    }

    /**
     * setReason
     * @param reason String - reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * getLocation
     * @return Location - location of mood
     */
    public Location getLocation() {
        return location;
    }

    /**
     * setLocation
     * @param location Location - location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * getSocialSituation
     * @return String - social situation of the mood
     */
    public String getSocialSituation() {
        return socialSituation;
    }

    /**
     * setSocialSituation
     * @param socialSituation String - social situation to set
     */
    public void setSocialSituation(String socialSituation) {
        this.socialSituation = socialSituation;
    }

    /**
     * hasImage
     * @return boolean - indicating whether the mood has an image
     */
    public boolean hasImage() {
        return hasImage;
    }

    /**
     * setHasImage
     * @param hasImage boolean - what to set hasImage to
     */
    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    /**
     * getMood
     * @return String - representing the mood
     */
    public String getMood() {
        return mood;
    }

    /**
     * setMood
     * @param mood String - what to set mood to
     */
    public void setMood(String mood) {
        this.mood = mood;
    }

    /**
     * getIcon
     * @return String - mood's icon in String form
     */
    public String getIcon() {
        return icon;
    }

    /**
     * setIcon
     * @param icon String - icon to set in String notation
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * getColor
     * @return color - mood's color
     */
    public String getColor() {
        return color;
    }

    /**
     * setColor
     * @param color color - to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * print
     * Simply prints relevant information about the mood
     */
    public void print() {
        Log.i("", "ID : " + this.id);
        Log.i("", "Mood : " + this.mood);
        Log.i("", "Social Situation : " + this.socialSituation);
        Log.i("", "Reason : " + this.reason);
        Log.i("", "Date : " + new Date(this.dateTimestamp).toString());
        Log.i("", "Emoji : " + this.icon);
    }
}
