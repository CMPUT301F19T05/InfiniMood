package com.example.infinimood.view;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.infinimood.R;

public class MoodCreateEditActivity extends MoodCompatActivity {

    DatePicker datePicker;
    TimePicker timePicker;
    Spinner moodSpinner;
    EditText reasonInput;
    Spinner socialSituInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeditmood);
    }

}
