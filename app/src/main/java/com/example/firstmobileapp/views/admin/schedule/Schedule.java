package com.example.firstmobileapp.views.admin.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.ScheduleModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Schedule extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference schedules = reference.child("schedules");

    private Button addSchButton, removeSchButton;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        addSchButton = findViewById(R.id.addScheduleButton);
        removeSchButton = findViewById(R.id.removeScheduleButton);
        linearLayout = findViewById(R.id.linearLayout);

        removeSchButton.setOnClickListener(view -> intents.adminScheduleRemoveIntent());

        addSchButton.setOnClickListener(view -> intents.adminScheduleAddIntent());

        schedules.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for(DataSnapshot data : snapshot.getChildren()){
                        ScheduleModel sch = new ScheduleModel(data.getValue().toString());
                        String time = sch.getTime();

                        enterTimes(time);
                    }
                    setSpaceLayout();
                }catch (DatabaseException e){
                    Log.e("error", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Schedule.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminMainIntent();
    }

    private void enterTimes(String time){
        TextView schText = new TextView(Schedule.this);
        schText.setText(time);
        schText.setTextSize(40);
        schText.setGravity(Gravity.CENTER);
        schText.setTextColor(getResources().getColor(R.color.white));

        linearLayout.addView(schText);
    }

    private void setSpaceLayout(){
        Space space = new Space(Schedule.this);
        space.setMinimumHeight(60);
        linearLayout.addView(space);
    }
}