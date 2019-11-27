package com.example.infinimood.controller;

import android.graphics.Bitmap;

/**
 * Interface for bitmap callbacks, to be implemented by any methods
 * needing a callback with a bitmap value
 */

public interface BitmapCallback {
    void onCallback(Bitmap bitmap);
}
