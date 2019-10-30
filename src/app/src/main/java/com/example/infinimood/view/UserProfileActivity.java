package com.example.infinimood.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.infinimood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserProfileActivity extends MoodCompatActivity {

    TextView textViewUsername;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            db.collection("users")
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
        final Intent intent = new Intent(this, MoodCreateEditActivity.class);
        startActivity(intent);
    }

    public void onMoodHistoryClicked(View view) {
        toast("TODO: Not implemented");
    }

    public void onLogoutClicked(View view) {
        firebaseAuth.signOut();
        startActivityNoHistory(MainActivity.class);
    }

    // generate a random new mood event for the user
    public void onGenerateMoodClicked(View view) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Random random = new Random();

        ArrayList<String> emotions = new ArrayList<String>();
        emotions.add("Happy");
        emotions.add("Sad");
        emotions.add("Angry");
        emotions.add("Scared");
        emotions.add("Disgusted");

        ArrayList<String> social_situations = new ArrayList<String>();
        social_situations.add("Alone");
        social_situations.add("With 1 other");
        social_situations.add("With several others");
        social_situations.add("In a crowd");

        String mood_id = String.valueOf(random.nextInt());
        String mood_emotion = emotions.get(Math.abs(random.nextInt() % emotions.size()));
        String mood_social_situation = social_situations.get(Math.abs(random.nextInt() % social_situations.size()));
        String mood_date = new Date().toString();

        Map<String, Object> mood = new HashMap<>();
        mood.put("id", mood_id);
        mood.put("mood", mood_emotion);
        mood.put("social_situation", mood_social_situation);
        mood.put("date", mood_date);

        db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("moods").add(mood);
    }

    // print all of the current user's mood events to console
    public void onPrintMoodsClicked(View view) {
        CollectionReference moods = db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("moods");
        moods.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getId() + " => " + document.getData());
                    }
                } else {
                    System.out.println("Error getting documents");
                }
            }
        });
    }
}
