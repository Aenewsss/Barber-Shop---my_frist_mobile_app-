package com.example.firstmobileapp.views.client;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class Account extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference clients = reference.child("clients");

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText usernameToEdit, phoneNumberToEdit;
    private Button saveChangesButton;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        toolbar = findViewById(R.id.navigation_toolbar);
        drawerLayout = findViewById(R.id.navigation_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        usernameToEdit = findViewById(R.id.username_field);
        phoneNumberToEdit = findViewById(R.id.phone_number_field);
        saveChangesButton = findViewById(R.id.save_changes_button);

        fillUserInfo();
        configSidebar();

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_main:
                    intents.clientMainIntent();
                    break;
                case R.id.nav_account:
                    break;
                case R.id.nav_appointments:
                    intents.clientAppointmentIntent();
                    break;
                case R.id.nav_logout:
                    intents.loginIntent();
                    break;
            }
            return true;
        });

        saveChangesButton.setOnClickListener(view -> {
            clients.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data : snapshot.getChildren()){
                        User client = data.getValue(User.class);

                        String email  = client.getEmail();
                        String currentEmail = fAuth.getCurrentUser().getEmail();

                        if(currentEmail.equals(email)){
                            String username = usernameToEdit.getText().toString();
                            String phoneNumber = phoneNumberToEdit.getText().toString();

                            if(!username.equals(client.getUsername())){
                                clients.child(data.getKey()).child("username").setValue(username);
                            }
                            if(!phoneNumber.equals(client.getPhone())){
                                clients.child(data.getKey()).child("phone").setValue(phoneNumber);
                            }
                            if(username.equals(client.getUsername()) && phoneNumber.equals(client.getPhone())){
                                Toast.makeText(Account.this, "No changes were made", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Account.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
            });
        });
    }

    private void fillUserInfo() {
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    User client = data.getValue(User.class);

                    String email  = client.getEmail();
                    String currentEmail = fAuth.getCurrentUser().getEmail();

                    if(currentEmail.equals(email)){
                        usernameToEdit.setText(client.getUsername());
                        phoneNumberToEdit.setText(client.getPhone());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Account.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
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

        navigationView.setCheckedItem(R.id.nav_account);
    }


}