package com.example.firstmobileapp.views.admin.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;

public class Settings extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminMainIntent();
    }
}