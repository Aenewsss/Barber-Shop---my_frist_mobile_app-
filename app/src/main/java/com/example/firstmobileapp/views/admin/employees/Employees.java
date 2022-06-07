package com.example.firstmobileapp.views.admin.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;

public class Employees extends AppCompatActivity {
    private final Intents intents = new Intents(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminMainIntent();
    }
}