package com.example.infinimood.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.infinimood.R;
import com.example.infinimood.controller.BooleanCallback;
import com.example.infinimood.controller.GetUsersCallback;
import com.example.infinimood.controller.UserAdapter;
import com.example.infinimood.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * UsersActivity.java
 * Activity for searching users, and viewing users you follow and that follow you, as well as
 * dealing with follow requests
 */
public class UsersActivity extends MoodCompatActivity  {

    private static final String TAG = "UsersActivity";

    private ListView searchListView;
    private SearchView searchView;
    private UserAdapter searchAdapter;
    private Spinner modeSpinner;
    private FrameLayout progressOverlayContainer;

    private String userId;

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<User> currentlyShownUsers = new ArrayList<>();
    BottomNavigationView navigationView;

    /**
     * onCreate
     * Ovverides onCreate. Gets the activity ready. Runs when activity is created.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_users);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().getItem(0).setChecked(true);

        progressOverlayContainer = findViewById(R.id.progressOverlayContainer);

        if (!firebaseController.userAuthenticated()) {
            startActivityNoHistory(LoginActivity.class);
        }

        userId = firebaseController.getCurrentUID();

        modeSpinner = findViewById(R.id.searchSpinner);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {
                currentlyShownUsers.clear();

                String mode = (String) modeSpinner.getItemAtPosition(j);
                if (mode.equals("All")) {
                    for (int i = 0; i < users.size(); ++i) {
                        User user = users.get(i);
                        if (!user.getUserID().equals(userId)) {
                            currentlyShownUsers.add(user);
                        }                      }
                }
                else if (mode.equals("Followers")) {
                    for (int i = 0; i < users.size(); ++i) {
                        User user = users.get(i);
                        if (user.isFollowsCurrentUser() || user.isRequestedFollowCurrentUser()) {
                            if (!user.getUserID().equals(userId)) {
                                currentlyShownUsers.add(user);
                            }                            }
                    }
                }
                else if (mode.equals("Following")) {
                    for (int i = 0; i < users.size(); ++i) {
                        User user = users.get(i);
                        if (user.isCurrentUserFollows() || user.isCurrentUserRequestedFollow()) {
                            if (!user.getUserID().equals(userId)) {
                                currentlyShownUsers.add(user);
                            }                              }
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
        searchListView = findViewById(R.id.searchListView);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) searchListView.getItemAtPosition(position);
                if (user.isCurrentUserFollows()) {
                    Intent i = new Intent(UsersActivity.this, MoodHistoryActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }
            }
        });

        showOverlay();
        firebaseController.getUsers(new GetUsersCallback() {
            @Override
            public void onCallback(ArrayList<User> usersArrayList) {
                users = usersArrayList;
                currentlyShownUsers = new ArrayList<User>();
                for (int i = 0; i < users.size(); ++i) {
                    User user = users.get(i);
                    if (!user.getUserID().equals(userId)) {
                        currentlyShownUsers.add(user);
                    }
                }
                hideOverlay();
                update();
            }
        });
    }

    /**
     * findUsersBySubstring
     * finds users and updates ListView
     * @param substring String - string to search for
     */
    public void findUsersBySubstring(String substring) {
        currentlyShownUsers.clear();
        for (final User user : users) {
            if (user.getUsername().contains(substring)) {
                currentlyShownUsers.add(user);
            }
        }
        update();
    }

    /**
     * onFollowClicked
     * Sends a follow request through firebase
     * @param view View
     */
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

    /**
     * onAcceptClicked
     * Accept the follow request through firebase
     * @param view View
     */
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

    /**
     * onDeclineClicked
     * Decline the follow request through firebase
     * @param view View
     */
    public void onDeclineClicked(View view) {
        View item = (View) view.getParent();
        int pos = searchListView.getPositionForView(item);

        User user = searchAdapter.getItem(pos);

        firebaseController.declineFollowRequest(user, new BooleanCallback() {
            @Override
            public void onCallback(boolean success) {
                if (success) {
                    toast("Successfully declined follow request");
                    user.setRequestedFollowCurrentUser(false);
                }
                else {
                    toast("Failed to decline follow request");
                }

                update();
            }
        });
    }

    /**
     * onUnfollowClicked
     * Unfollow user through firebase
     * @param view View
     */
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

    /**
     * showOverlay
     */
    public void showOverlay() {
        progressOverlayContainer.setVisibility(View.VISIBLE);
        progressOverlayContainer.bringToFront();
    }

    /**
     * hideOverlay
     */
    public void hideOverlay() {
        progressOverlayContainer.setVisibility(View.GONE);
    }

    /**
     * update
     * update the ListView
     */
    public void update() {
        searchAdapter = new UserAdapter(UsersActivity.this, currentlyShownUsers);
        searchListView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();
    }

    /**
     * onSearchUsersClicked
     * Starts UsersActivity
     * @param item MenuItem
     */
    // We should have a NavBar class for these methods
    public void onSearchUsersClicked(MenuItem item) {
        final Intent intent = new Intent(this, UsersActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    /**
     * onAddMoodClicked
     * Starts AddEditMoodActivity
     * @param item MenuItem
     */
    public void onAddMoodClicked(MenuItem item) {
        final Intent intent = new Intent(this, AddEditMoodActivity.class);
        intent.putExtra("requestCode", ADD_MOOD);
        item.setChecked(true);
        startActivity(intent);
    }

    /**
     * onMoodHistoryClicked
     * Starts MoodHistoryActivity
     * @param item MenuItem
     */
    public void onMoodHistoryClicked(MenuItem item) {
        final Intent intent = new Intent(this, MoodHistoryActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }

    /**
     * onUserProfileClicked
     * Starts UserProfileActivity
     * @param item MenuItem
     */
    public void onUserProfileClicked(MenuItem item) {
        final Intent intent = new Intent(this, UserProfileActivity.class);
        item.setChecked(true);
        startActivity(intent);
    }
}
