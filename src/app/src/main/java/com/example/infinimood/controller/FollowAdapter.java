package com.example.infinimood.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.infinimood.R;
import com.example.infinimood.model.User;

import java.util.ArrayList;

/**
 *  FollowAdapter.java
 *  Adapter for the ListView in FollowersActivity
 */

public class FollowAdapter extends ArrayAdapter<User>  {


    private ArrayList<User> followers;
    private ArrayList<User> following;
    private ArrayList<Boolean> AcceptedList;
    private Context context;

    public FollowAdapter(Context context,ArrayList <User> followers,ArrayList<User> following,ArrayList<Boolean> AcceptedList){
        super(context,0, followers);
        this.context = context;
        this.followers = followers;
        this.following = following;
        this.AcceptedList = AcceptedList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User follower = followers.get(position);
        Boolean Accepted = AcceptedList.get(position);
        View view = convertView;
        LayoutInflater inflator = ((android.app.Activity) this.context).getLayoutInflater();
        view = inflator.inflate(R.layout.follower_entry, parent, false);

        TextView followerUsername = view.findViewById(R.id.follower_username);
        followerUsername.setText(follower.getUsername());
        Button followButton = view.findViewById(R.id.follow_button);
        Button AcceptedButton = view.findViewById(R.id.accept_button);
        TextView followingStatus = view.findViewById(R.id.following_status);
        if (Accepted){
            Log.i("test","a");
            AcceptedButton.setVisibility(View.INVISIBLE);
        }
        else{
            AcceptedButton.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i<following.size(); i++){
            if (following.get(i).getUserID().equals(follower.getUserID())){
                followButton.setVisibility(View.INVISIBLE);
                followingStatus.setVisibility(View.VISIBLE);
                return view;
            }
            else{
                followButton.setVisibility(View.VISIBLE);
                followingStatus.setVisibility(View.INVISIBLE);
            }
        }


        return view;
    }
}
