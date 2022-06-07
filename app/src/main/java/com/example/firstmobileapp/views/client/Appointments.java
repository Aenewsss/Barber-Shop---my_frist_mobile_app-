package com.example.firstmobileapp.views.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.ScheduleModel;
import com.example.firstmobileapp.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Appointments extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference clients = reference.child("clients");
    private final DatabaseReference dates = reference.child("dates");

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayout linearLayout;
    private Button removeAppointmentButton;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        toolbar = findViewById(R.id.navigation_toolbar);
        drawerLayout = findViewById(R.id.navigation_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        linearLayout = findViewById(R.id.linear_layout_appointments);
        removeAppointmentButton = findViewById(R.id.remove_appointment_button);

        fillListAppointments();

        configSidebar();

        removeAppointmentButton.setOnClickListener(view -> {
            clients.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data : snapshot.getChildren()){
                        User client = data.getValue(User.class);

                        if(client.getEmail().equals(fAuth.getCurrentUser().getEmail())){
                            clients.child(data.getKey()).child("appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot data : snapshot.getChildren()){
                                        ScheduleModel schedule = data.getValue(ScheduleModel.class);
                                        dates.child(data.getKey()).child(schedule.getTime()).setValue(true);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Appointments.this, error.getMessage(), Toast.LENGTH_SHORT).show();}
                            });
                            clients.child(data.getKey()).child("appointments").removeValue();
                            intents.clientAppointmentIntent();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Appointments.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
            });
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_main:
                    intents.clientMainIntent();
                    break;
                case R.id.nav_account:
                    intents.clientAccountIntent();
                    break;
                case R.id.nav_appointments:
                    break;
                case R.id.nav_logout:
                    intents.loginIntent();
                    break;
            }
            return true;
        });
    }

    private void fillListAppointments() {
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentEmail = fAuth.getCurrentUser().getEmail();
                for(DataSnapshot data : snapshot.getChildren()){
                    User client = data.getValue(User.class);

                    String email = client.getEmail();

                    if(currentEmail.equals(email)){
                        clients.child(data.getKey()).child("appointments").addValueEventListener(new ValueEventListener() {
                            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getChildrenCount() != 0) {
                                    for (DataSnapshot data : snapshot.getChildren()) {

                                        ScheduleModel sch = data.getValue(ScheduleModel.class);

                                        String date = data.getKey();
                                        String time = sch.getTime();

                                        TextView text = new TextView(Appointments.this);

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );

                                        params.setMargins(5, 100, 5, 40);

                                        text.setText("Date: " + date + " | Time: " + time);
                                        text.setTextSize(25);
                                        text.setTextColor(getResources().getColor(R.color.white));
                                        text.setGravity(Gravity.CENTER);
                                        text.setLayoutParams(params);
                                        linearLayout.addView(text);
                                        removeAppointmentButton.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    TextView text = new TextView(Appointments.this);
                                    Button button = new Button(Appointments.this);

                                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    textParams.setMargins(5, 100, 5, 100);

                                    text.setText("You don't have appointments");
                                    text.setTextSize(25);
                                    text.setTextColor(getResources().getColor(R.color.white));
                                    text.setGravity(Gravity.CENTER);
                                    text.setLayoutParams(textParams);
                                    linearLayout.addView(text);

                                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    buttonParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                                    buttonParams.gravity = Gravity.CENTER;

                                    button.setText("Make an appointment");
                                    button.setBackground(getResources().getDrawable(R.drawable.styled_button1));
                                    button.setTextColor(getResources().getColor(R.color.white));
                                    button.setTextSize(20);
                                    button.setOnClickListener(view -> intents.clientMainIntent());
                                    button.setLayoutParams(buttonParams);
                                    linearLayout.addView(button);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Appointments.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Appointments.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
        });
    }

    private void configSidebar() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(this);
        arrowDrawable.setColor(getResources().getColor(R.color.purple_99));
        arrowDrawable.setBarLength(100);
        arrowDrawable.setGapSize(20);

        toggle.setDrawerArrowDrawable(arrowDrawable);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_appointments);
    }
}