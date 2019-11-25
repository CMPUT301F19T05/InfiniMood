package com.example.infinimood.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.BitmapCallback;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.model.Mood;
import com.example.infinimood.view.AddEditMoodActivity;
import com.example.infinimood.view.ViewLocationActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewImageFragment extends DialogFragment {

    private static final String TAG = "ViewImageFragment";

    private FirebaseController firebaseController = new FirebaseController();

    private Mood mood = null;
    private Bitmap bitmap = null;

    public ViewImageFragment(Mood mood) {
        this.mood = mood;
    }

    public ViewImageFragment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_image, null);

        ImageView imageView = view.findViewById(R.id.viewImageFragmentImageView);

        if (mood == null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else if (mood != null && bitmap == null) {
            firebaseController.getImageFromDB(this.mood, new BitmapCallback() {
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

        return builder.create();
    }
}
