package com.example.firstmobileapp.views.admin.schedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScheduleRemove extends AppCompatActivity {
    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference schedules = reference.child("schedules");

    private EditText removeScheduleText;
    private Button scheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_remove);

        removeScheduleText = findViewById(R.id.removeScheduleText);
        scheduleButton = findViewById(R.id.remove_schedule_button);

        scheduleButton.setOnClickListener(view -> {
            String schedule = removeScheduleText.getText().toString();

            if(TextUtils.isEmpty(schedule)){
                Toast.makeText(ScheduleRemove.this, "Please, fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if(schedule.length() != 5 || schedule.indexOf(":") != 2){
                Toast.makeText(ScheduleRemove.this, "Enter th time in HH/mm format", Toast.LENGTH_SHORT).show();
                return;
            }
            schedules.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data : snapshot.getChildren()){
                        if(data.getKey().equals(schedule)){
                            schedules.child(schedule).removeValue();
                            intents.adminScheduleIntent();
                            return;
                        }
                    }
                    Toast.makeText(ScheduleRemove.this, "This time doesn't exist", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(ScheduleRemove.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
            });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminScheduleIntent();
    }
}