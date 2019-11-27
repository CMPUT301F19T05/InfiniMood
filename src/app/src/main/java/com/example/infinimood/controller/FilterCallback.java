package com.example.infinimood.controller;

import java.util.HashSet;

/**
 * FilterCallback
 * An interface for users callbacks, to be implemented by any methods needing a callback with
 * a list of filtered items
 */
public interface FilterCallback {
    void onCallback(HashSet<String> filter);
}
