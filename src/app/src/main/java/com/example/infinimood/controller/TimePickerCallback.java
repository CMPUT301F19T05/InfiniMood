package com.example.infinimood.controller;

/**
 * TimePickerCallback
 * An interface for Time callbacks, to be implemented by any methods needing a callback with
 * a Time data.
 */
public interface TimePickerCallback {
    void OnCallback(int hourOfDay, int minute);
}
