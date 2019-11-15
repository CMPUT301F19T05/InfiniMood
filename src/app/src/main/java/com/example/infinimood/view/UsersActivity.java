package com.example.infinimood.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.GetUsersCallback;
import com.example.infinimood.controller.UserAdapter;
import com.example.infinimood.model.User;

import java.util.ArrayList;

public class UsersActivity extends MoodCompatActivity  {

    private static final String TAG = "UsersActivity";

    private ListView searchListView;
    private SearchView searchView;
    private UserAdapter searchAdapter;
    private Spinner modeSpinner;

    private ArrayList<User> users;
    private ArrayList<User> currentlyShownUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_users);

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(MainActivity.class);
        }

        firebaseController.getUsers(new GetUsersCallback() {
            @Override
            public void onCallback(ArrayList<User> usersArrayList) {
                users = usersArrayList;
                currentlyShownUsers = new ArrayList<User>();
                for (int i = 0; i < users.size(); ++i) {
                    User user = users.get(i);
                    currentlyShownUsers.add(user);
                }

                searchListView = findViewById(R.id.searchListView);

                modeSpinner = findViewById(R.id.searchSpinner);
                modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {
                        currentlyShownUsers.clear();

                        String mode = (String) modeSpinner.getItemAtPosition(j);
                        if (mode.equals("All")) {
                            for (int i = 0; i < users.size(); ++i) {
                                User user = users.get(i);
                                currentlyShownUsers.add(user);
                            }
                        }
                        else if (mode.equals("Followers")) {
                            for (int i = 0; i < users.size(); ++i) {
                                User user = users.get(i);
                                if (user.isFollowsCurrentUser() || user.isRequestedFollowCurrentUser()) {
                                    currentlyShownUsers.add(user);
                                }
                            }
                        }
                        else if (mode.equals("Following")) {
                            for (int i = 0; i < users.size(); ++i) {
                                User user = users.get(i);
                                if (user.isCurrentUserFollows() || user.isCurrentUserRequestedFollow()) {
                                    currentlyShownUsers.add(user);
                                }
                            }
                        }

                        update();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.e(TAG, "This shouldn't happen (empty mode spinner)");
                    }
                });

                searchView = findViewById(R.id.searchSearchView);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) { return false; }

                    @Override
                    public boolean onQueryTextChange(String substring) {
                        findUsersBySubstring(substring);
                        update();
                        return false;
                    }
                });

                update();
            }
        });


    }

    public void findUsersBySubstring(String substring) {
        for (int i = 0; i < currentlyShownUsers.size(); ++i) {
            User user = currentlyShownUsers.get(i);
            if (!user.getUsername().contains(substring)) {
                currentlyShownUsers.remove(i);
                continue;
            }
        }
        update();
    }

    public void onFollowClicked(View view) {
        View item = (View) view.getParent();
        int pos = searchListView.getPositionForView(item);

        User user = searchAdapter.getItem(pos);

        firebaseController.requestToFollow(user, new BooleanCallback() {
            @Override
            public void onCallback(boolean success) {
                if (success) {
                    toast("Successfully sent follow request");
                    user.setCurrentUserRequestedFollow(true);
                }
                else {
                    toast("Failed to send follow request");
                }

                update();
            }
        });
    }

    public void onAcceptClicked(View view) {
        View item = (View) view.getParent();
        int pos = searchListView.getPositionForView(item);

        User user = searchAdapter.getItem(pos);

        firebaseController.acceptFollowRequest(user, new BooleanCallback() {
            @Override
            public void onCallback(boolean success) {
                if (success) {
                    toast("Successfully accepted follow request");
                    user.setFollowsCurrentUser(true);
                    user.setRequestedFollowCurrentUser(false);
                }
                else {
                    toast("Failed to accept follow request");
                }

                update();
            }
        });
    }

    public void onUnfollowClicked(View view) {
        View item = (View) view.getParent();
        int pos = searchListView.getPositionForView(item);

        User user = searchAdapter.getItem(pos);

        firebaseController.unfollowUser(user, new BooleanCallback() {
            @Override
            public void onCallback(boolean success) {
                if (success) {
                    toast("Successfully unfollowed user");
                    user.setCurrentUserFollows(false);
                }
                else {
                    toast("Failed to unfollow user");
                }

                update();
            }
        });
    }

    public void update() {
        searchAdapter = new UserAdapter(UsersActivity.this, currentlyShownUsers);
        searchListView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();
    }
}