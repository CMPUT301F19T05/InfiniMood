package com.example.infinimood.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.BitmapCallback;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.model.Mood;
import com.example.infinimood.view.AddEditMoodActivity;

/**
 * ViewImageFragment.java
 * Fragment for displaying an image. Used by MoodHistoryFragment
 */
public class ViewImageFragment extends DialogFragment {

    private static final String TAG = "ViewImageFragment";

    private FirebaseController firebaseController = new FirebaseController();

    private Mood mood = null;
    private Bitmap bitmap = null;

    private BooleanCallback deleteCallback = null;

    /**
     * ViewImageFragment
     * Simple constructor for ViewImageFragment
     * @param mood Mood - whose image we want to view
     */
    public ViewImageFragment(Mood mood) {
        this.mood = mood;
    }

    /**
     * ViewImageFragment
     * Simple constructor for ViewImageFragment
     * @param bitmap bitmap - of the image we wish to view
     */
    public ViewImageFragment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * ViewImageFragment
     * Constructor for ViewImageFragment
     * @param mood Mood - whose image we want to view
     * @param deleteCallback boolean - callback for indicating success or failure
     */
    public ViewImageFragment(Mood mood, BooleanCallback deleteCallback) {
        this.mood = mood;
        this.deleteCallback = deleteCallback;
    }

    /**
     * ViewImageFragment
     * Constructor for ViewImageFragment
     * @param bitmap Bitmap - Bitmap of the image we wish to view
     * @param deleteCallback boolean - callback for indicating success or failure
     */
    public ViewImageFragment(Bitmap bitmap, BooleanCallback deleteCallback) {
        this.bitmap = bitmap;
        this.deleteCallback = deleteCallback;
    }

    /**
     * onCreateDialog
     * Overrides onCreateDialog method. Displays the image.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_image, null);

        ImageView imageView = view.findViewById(R.id.viewImageFragmentImageView);

        if (mood == null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else if (mood != null && bitmap == null) {
            firebaseController.getMoodImageFromDB(this.mood, new BitmapCallback() {
                @Override
                public void onCallback(Bitmap bitmap) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        getActivity().getFragmentManager().popBackStack();
                    }
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view);
        builder.setPositiveButton("OK", null);

        if (deleteCallback != null) {
            builder.setNegativeButton("REMOVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteCallback.onCallback(true);
                }
            });
        }

        return builder.create();
    }
}
