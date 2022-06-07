package com.example.firstmobileapp.views.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends AppCompatActivity implements CalendarView.OnDateChangeListener, NavigationView.OnNavigationItemSelectedListener {

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference dates = reference.child("dates");
    private final DatabaseReference schedules = reference.child("schedules");
    private final DatabaseReference clients = reference.child("clients");

    private TextView helloText;
    private CalendarView calendar;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private final Intents intents = new Intents(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view);

        helloText = findViewById(R.id.helloText);
        calendar = findViewById(R.id.calendar);
        drawerLayout = findViewById(R.id.drawerLayout_clientView);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        fillHelloText();

        setCalendarDate();

        configSidebar();

        checkDateAppointments();

        navigationView.setNavigationItemSelectedListener(this);

        calendar.setOnDateChangeListener(this);
    }

    private void checkDateAppointments() {
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    User client = data.getValue(User.class);

                    String currentEmail = fAuth.getCurrentUser().getEmail();

                    String clientKey = data.getKey();

                    if(currentEmail.equals(client.getEmail())){
                        clients.child(clientKey).child("appointments").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot data : snapshot.getChildren()){
                                    Date todayDate = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                                    String dateFormatted = formatter.format(todayDate);

                                    String todayDateDay = dateFormatted.substring(0,2);
                                    String todayDateMonth = dateFormatted.substring(3,5);

                                    int firstIndex = data.getKey().indexOf("-");
                                    int secondIndex = data.getKey().indexOf("-", 3);

                                    String monthDb = data.getKey().substring(firstIndex+1, secondIndex);
                                    String dayDb = data.getKey().substring(0, firstIndex);

                                    if((Integer.parseInt(monthDb) < Integer.parseInt(todayDateMonth)) || ((Integer.parseInt(monthDb) == Integer.parseInt(todayDateMonth)) && (Integer.parseInt(dayDb) < Integer.parseInt(todayDateDay)))){
                                        clients.child(clientKey).child("appointments").child(data.getKey()).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Main.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Main.this, error.getMessage(), Toast.LENGTH_SHORT).show();            }
        });
    }

    private void setCalendarDate() {
        Date todayDate = new Date();
        calendar.setMinDate(todayDate.getTime());
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

        navigationView.setCheckedItem(R.id.nav_main);
    }

    private void fillHelloText() {
        String currentEmail = fAuth.getCurrentUser().getEmail();

        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    User client = data.getValue(User.class);

                    String email = client.getEmail();

                    if(email.equals(currentEmail)){
                        helloText.append(client.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
        month++;
        String date = day + "-" + month + "-" + year;

        dates.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(date)) {
                    schedules.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                dates.child(date).child(data.getValue().toString()).setValue(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Main.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                showMarkIntent(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMarkIntent(@NonNull String date){
        String dateConvert = date.replace("-", "/");

        Intent markIntent = new Intent(Main.this, Mark.class);
        markIntent.putExtra("dateConvert", dateConvert);
        markIntent.putExtra("date", date);
        startActivity(markIntent);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_main:
                break;
            case R.id.nav_account:
                intents.clientAccountIntent();
                break;
            case R.id.nav_appointments:
                intents.clientAppointmentIntent();
                break;
            case R.id.nav_logout:
                intents.loginIntent();
                break;
        }
        return true;
    }
}