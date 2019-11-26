package com.example.infinimood.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infinimood.R;
import com.example.infinimood.controller.BitmapCallback;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.StringCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

/**
 * UserProfileActivity.java
 * Homepage when authenticated
 * Access point for most functionality
 */

public class UserProfileActivity extends MoodCompatActivity {

    private static final String TAG = "UserProfileActivity";

    private boolean defaultPicture = true;

    TextView textViewUsername;
    ImageView profileImageView;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImageView = findViewById(R.id.profileImageView);
        firebaseController.getProfileImageFromDB(new BitmapCallback() {
            @Override
            public void onCallback(Bitmap bitmap) {
                if (bitmap != null) {
                    profileImageView.setImageBitmap(bitmap);
                    defaultPicture = false;
                } else {
                    profileImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_picture));
                    defaultPicture = true;
                }
            }
        });

        textViewUsername = findViewById(R.id.profileUsernameTextView);
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().getItem(3).setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(LoginActivity.class);
        }

        firebaseController.getUsername(new StringCallback() {
            @Override
            public void onCallback(String username) {
                textViewUsername.setText(username);
            }
        });
    }

    // handle the result of selecting images and locations
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                firebaseController.uploadProfileImageToDB(bitmap, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean bool) {
                        if (bool) {
                            toast("Successfully updated profile picture");
                            profileImageView.setImageBitmap(bitmap);
                            defaultPicture = false;
                        } else {
                            toast("Failed to update profile picture");
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onUploadImageClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    public void onDeleteImageClicked(View view) {
        if (!defaultPicture) {
            firebaseController.deleteProfileImageFromDB(new BooleanCallback() {
                @Override
                public void onCallback(boolean bool) {
                    if (bool) {
                        toast("Successfully deleted profile picture");
                        profileImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_picture));
                        defaultPicture = true;
                    } else {
                        toast("Failed to delete profile picture");
                    }
                }
            });
        } else {
            toast("Upload a profile picture");
        }
    }

    public void onAddMoodClicked(MenuItem item) {
        final Intent intent = new Intent(this, AddEditMoodActivity.class);
        intent.putExtra("requestCode", ADD_MOOD);
        item.setChecked(true);
        startActivity(intent);
    }

    public void onMoodHistoryClicked(MenuItem item) {
        final Intent intent = new Intent(this, MoodHistoryActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    public void onSearchUsersClicked(MenuItem item) {
        final Intent intent = new Intent(this, UsersActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    public void onUserProfileClicked(MenuItem item) {
        final Intent intent = new Intent(this, UserProfileActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    public void onLogoutClicked(View view) {
        firebaseController.signOut();
        startActivityNoHistory(LoginActivity.class);
    }
}
