package com.example.infinimood.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserProfileActivity extends MoodCompatActivity {

    private static final String TAG = "UserProfileActivity";

    TextView textViewUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textViewUsername = findViewById(R.id.text_view_username);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser == null) {
            startActivityNoHistory(MainActivity.class);
        } else {
            firebaseFirestore.collection("users")
                    .document(firebaseUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot result = task.getResult();
                                if (result != null) {
                                    Object username = result.get("username");
                                    if (username != null) {
                                        textViewUsername.setText(username.toString());
                                        return;
                                    }
                                }
                            }

                            textViewUsername.setText(R.string.default_username);
                        }
                    });
        }
    }

    public void onAddMoodClicked(View view) {
        final Intent intent = new Intent(this, AddEditMoodActivity.class);
        startActivity(intent);
    }

    public void onMoodHistoryClicked(View view) {
        final Intent intent = new Intent(this, MoodHistoryActivity.class);
        startActivity(intent);
    }

    public void onLogoutClicked(View view) {
        firebaseAuth.signOut();
        startActivityNoHistory(MainActivity.class);
    }

    // print all of the current user's mood events to console
    public void onPrintMoodsClicked(View view) {
        refreshUserMoods();
        Log.i("", "===========================================");
        for (int i = 0; i < moods.size(); i++) {
            moods.get(i).print();
            Log.i("", "===========================================");
        }
    }
}
