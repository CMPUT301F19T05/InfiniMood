package com.example.infinimood.controller;

import com.example.infinimood.model.Mood;

/**
 * GetMoodCallback
 * An interface for mood callbacks, to be implemented by any methods needing a callback with
 * Mood information
 */
public interface GetMoodCallback {
    void onCallback(Mood mood);
}
