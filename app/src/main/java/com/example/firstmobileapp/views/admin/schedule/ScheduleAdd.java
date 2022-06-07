package com.example.firstmobileapp.views.admin.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScheduleAdd extends AppCompatActivity {
    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference schedules = reference.child("schedules");

    private Button addSchButton;
    private EditText inputTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);

        inputTime = findViewById(R.id.inputSchedule);
        addSchButton = findViewById(R.id.addSchButton);

        addSchButton.setOnClickListener(view -> {
            String time = inputTime.getText().toString();

            if(time.length() != 5 || time.indexOf(":") != 2){
                Toast.makeText(ScheduleAdd.this, "The time must be written in the format HH:mm", Toast.LENGTH_SHORT).show();
                return;
            }
            schedules.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(time)){
                        Toast.makeText(ScheduleAdd.this, "This time is already registered", Toast.LENGTH_SHORT).show();
                    }else{
                        schedules.child(time).setValue(time);
                        intents.adminScheduleIntent();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ScheduleAdd.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminScheduleIntent();
    }
}