package com.example.infinimood.controller;

/**
 * Interface for date callbacks, to be implemented by any methods
 * needing a callback with date information
 */

public interface DatePickerCallback {
    void OnCallback(int year, int month, int day);
}
