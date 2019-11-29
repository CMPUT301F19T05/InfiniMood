package com.example.infinimood.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.infinimood.R;
import com.example.infinimood.controller.BitmapCallback;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.FirebaseController;
import com.example.infinimood.model.User;
import com.example.infinimood.view.LoginActivity;
import com.example.infinimood.view.MoodHistoryActivity;

public class UserInfoFragment extends Fragment {

    private static FirebaseController firebaseController = new FirebaseController();

    private User user;

    private TextView usernameTextView;
    private Button followerMoodHistoryButton;
    private ImageView otherUserProfileImageView;
    private Button fragmentAcceptButton;
    private Button fragmentDeclineButton;
    private TextView fragmentFollowStatusTextView;
    private Button fragmentFollowButton;
    private Button fragmentUnfollowButton;
    private TextView fragmentRequestToFollowTextView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
        }
        usernameTextView = view.findViewById(R.id.usernameTextView);
        usernameTextView.setText(user.getUsername());
        fragmentAcceptButton = view.findViewById(R.id.fragment_accept_button);
        fragmentDeclineButton = view.findViewById(R.id.fragment_decline_button);
        fragmentFollowStatusTextView = view.findViewById(R.id.fragment_user_follow_status);
        fragmentFollowButton = view.findViewById(R.id.fragment_follow_button);
        fragmentUnfollowButton = view.findViewById(R.id.fragment_unfollow_button);
        fragmentRequestToFollowTextView = view.findViewById(R.id.fragment_follow_request_status);

        update();

        otherUserProfileImageView = view.findViewById(R.id.otherUserProfileImageView);
        firebaseController.getOtherUserProfileImageFromDB(user,new BitmapCallback() {
                    @Override
                    public void onCallback(Bitmap bitmap) {
                        if (bitmap != null) {
                            otherUserProfileImageView.setImageBitmap(bitmap);
                        } else {
                            otherUserProfileImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_picture));
                        }
                    }
                });


        followerMoodHistoryButton = view.findViewById(R.id.followerProfileMoodHistoryButton);
        if (user.isCurrentUserFollows()) {
            followerMoodHistoryButton.setVisibility(View.VISIBLE);
        } else{
            followerMoodHistoryButton.setVisibility(View.GONE);
        }

        followerMoodHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMoodHistoryFragment userMoodHistoryFragment = new UserMoodHistoryFragment();
                userMoodHistoryFragment.setArguments(bundle);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_user_profile,userMoodHistoryFragment)
                        .commit();

            }
        });
        fragmentFollowButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseController.requestToFollow(user, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean success) {
                        if (success) {
                            Toast.makeText(getActivity(), "Successfully sent follow request", Toast.LENGTH_SHORT);
                            user.setCurrentUserRequestedFollow(true);
                        } else {
                            Toast.makeText(getActivity(), "Failed to send follow request", Toast.LENGTH_SHORT);
                        }

                        update();
                    }
                });
            }
        }));
        fragmentUnfollowButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseController.unfollowUser(user, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean success) {
                        if (success) {
                            Toast.makeText(getActivity(),"Successfully unfollowed user",Toast.LENGTH_SHORT);
                            user.setCurrentUserFollows(false);
                        }
                        else {
                            Toast.makeText(getActivity(),"Failed to unfollow user",Toast.LENGTH_SHORT);
                        }

                        update();
                    }
                });
            }
        }));

        fragmentAcceptButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseController.acceptFollowRequest(user, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean success) {
                        if (success) {
                            Toast.makeText(getActivity(),"Successfully accepted follow request",Toast.LENGTH_SHORT);
                            user.setFollowsCurrentUser(true);
                            user.setRequestedFollowCurrentUser(false);
                        }
                        else {
                            Toast.makeText(getActivity(),"Failed to accept follow request",Toast.LENGTH_SHORT);
                        }

                        update();
                    }
                });
            }
        }));

        fragmentDeclineButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseController.declineFollowRequest(user, new BooleanCallback() {
                    @Override
                    public void onCallback(boolean success) {
                        if (success) {
                            Toast.makeText(getActivity(),"Successfully declined follow request",Toast.LENGTH_SHORT);
                            user.setRequestedFollowCurrentUser(false);
                        }
                        else {
                            Toast.makeText(getActivity(),"Failed to decline follow request",Toast.LENGTH_SHORT);
                        }
                        update();
                    }
                });
            }
        }));

        return view;
    }

    public void closeFrag(){
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
    }




    public void update() {
        firebaseController.userAuthenticated();
        if (user.isCurrentUserFollows()) {
            fragmentFollowButton.setVisibility(View.GONE);
            fragmentRequestToFollowTextView.setVisibility(View.GONE);
            fragmentUnfollowButton.setVisibility(View.VISIBLE);
        } else if (user.isCurrentUserRequestedFollow()) {
            fragmentFollowButton.setVisibility(View.GONE);
            fragmentUnfollowButton.setVisibility(View.GONE);

            fragmentRequestToFollowTextView.setVisibility(View.VISIBLE);
        } else {
            fragmentRequestToFollowTextView.setVisibility(View.GONE);
            fragmentUnfollowButton.setVisibility(View.GONE);

            fragmentFollowButton.setVisibility(View.VISIBLE);
        }

        // show / hide views based on whether they follow the current user
        if (user.isFollowsCurrentUser()) {
            fragmentAcceptButton.setVisibility(View.GONE);
            fragmentDeclineButton.setVisibility(View.GONE);

            fragmentFollowStatusTextView.setVisibility(View.VISIBLE);
        } else if (user.isRequestedFollowCurrentUser()) {
            fragmentFollowStatusTextView.setVisibility(View.GONE);

            fragmentAcceptButton.setVisibility(View.VISIBLE);
            fragmentDeclineButton.setVisibility(View.VISIBLE);
        } else {
            fragmentFollowStatusTextView.setVisibility(View.GONE);
            fragmentAcceptButton.setVisibility(View.GONE);
            fragmentDeclineButton.setVisibility(View.GONE);
        }
    }

}
