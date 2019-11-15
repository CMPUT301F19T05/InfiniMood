package com.example.infinimood.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.infinimood.R;
import com.example.infinimood.model.User;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User>  {


    private ArrayList<User> users;
    private Context context;

    public UserAdapter(Context context, ArrayList <User> users){
        super(context,0, users);

        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflator = ((android.app.Activity) this.context).getLayoutInflater();
        view = inflator.inflate(R.layout.user_entry, parent, false);

        // get views
        TextView usernameTextView = view.findViewById(R.id.userEntryUsernameTextView);

        Button acceptButton = view.findViewById(R.id.userEntryAcceptButton);
        TextView followsYouTextView = view.findViewById(R.id.userEntryFollowsYouTextView);

        Button followButton = view.findViewById(R.id.userEntryFollowButton);
        Button unfollowButton = view.findViewById(R.id.userEntryUnfollowButton);
        TextView requestedFollowTextView = view.findViewById(R.id.userEntryFollowingStatusTextView);

        // the user that the entry is for
        User user = users.get(position);

        // set username
        usernameTextView.setText(user.getUsername());

        // show / hide views based on whether the current user follows them
        if (user.isCurrentUserFollows()) {
            followButton.setVisibility(View.GONE);
            requestedFollowTextView.setVisibility(View.GONE);

            unfollowButton.setVisibility(View.VISIBLE);
        }
        else if (user.isCurrentUserRequestedFollow()) {
            followButton.setVisibility(View.GONE);
            unfollowButton.setVisibility(View.GONE);

            requestedFollowTextView.setVisibility(View.VISIBLE);
        }
        else {
            requestedFollowTextView.setVisibility(View.GONE);
            unfollowButton.setVisibility(View.GONE);

            followButton.setVisibility(View.VISIBLE);
        }

        // show / hide views based on whether they follow the current user
        if (user.isFollowsCurrentUser()) {
            acceptButton.setVisibility(View.GONE);

            followsYouTextView.setVisibility(View.VISIBLE);
        }
        else if (user.isRequestedFollowCurrentUser()) {
            followsYouTextView.setVisibility(View.GONE);

            acceptButton.setVisibility(View.VISIBLE);
        }
        else {
            followsYouTextView.setVisibility(View.GONE);
            acceptButton.setVisibility(View.GONE);
        }

        return view;
    }
}
