package com.example.infinimood.controller;

import com.example.infinimood.model.Mood;

import java.util.ArrayList;

public interface GetMoodsCallback {
    void onCallback(ArrayList<Mood> moodsArrayList);
}
