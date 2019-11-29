package com.example.infinimood.controller;

import com.example.infinimood.model.Mood;

import java.util.ArrayList;

/**
 * GetMoodsCallback
 * An interface for moods callbacks, to be implemented by any methods needing a callback with a
 * list of Moods.
 */
public interface GetMoodsCallback {
    void onCallback(ArrayList<Mood> moodsArrayList);
}
