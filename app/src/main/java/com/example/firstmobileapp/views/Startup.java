package com.example.firstmobileapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Startup extends AppCompatActivity {
    private final Intents intents = new Intents(this);

    private String admin_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        admin_email = getResources().getString(R.string.admin_email);

        new Handler().postDelayed(() -> checkUser(), 2000);
    }

    private void checkUser(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null){
            String email = currentUser.getEmail();

            if (email.equals(admin_email)) intents.adminMainIntent();
            else intents.clientMainIntent();
        }else{
            intents.loginIntent();
        }
    }

}