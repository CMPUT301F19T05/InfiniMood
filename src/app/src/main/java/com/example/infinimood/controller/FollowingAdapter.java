package com.example.infinimood.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.infinimood.R;
import com.example.infinimood.model.User;

import java.util.ArrayList;

/**
 *  FollowingAdapter.java
 *  Adapter for the ListView in FollowingActivity
 */

public class FollowingAdapter extends ArrayAdapter<User> {

    private ArrayList<User> followers;
    private ArrayList<User> following;
    private Context context;

    public FollowingAdapter(Context context,ArrayList <User> followers,ArrayList<User> following){
        super(context,0, following);
        this.context = context;
        this.followers = followers;
        this.following = following;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User followee = following.get(position);
        View view = convertView;
        LayoutInflater inflator = ((android.app.Activity) this.context).getLayoutInflater();
        view = inflator.inflate(R.layout.followee_entry, parent, false);

        TextView followeeUsername = view.findViewById(R.id.followee_username);
        followeeUsername.setText(followee.getUsername());

        return view;
    }
}