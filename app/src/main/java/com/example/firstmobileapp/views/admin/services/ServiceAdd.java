package com.example.firstmobileapp.views.admin.services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.Service;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ServiceAdd extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference services = reference.child("services");

    private EditText insertServiceName, insertServicePrice;
    private Button addServiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_add);

        insertServiceName = findViewById(R.id.insertServiceName);
        insertServicePrice = findViewById(R.id.insertServicePrice);
        addServiceButton = findViewById(R.id.addServiceButton);

        addServiceButton.setOnClickListener(view -> {
            String serviceType = insertServiceName.getText().toString(), price = insertServicePrice.getText().toString();

            if(TextUtils.isEmpty(serviceType) || TextUtils.isEmpty(price)){
                Toast.makeText(ServiceAdd.this, "Please, fill in all fields", Toast.LENGTH_SHORT).show();
            }else{
                Service service = new Service(serviceType, Double.parseDouble(price));
                services.push().setValue(service);
                intents.adminServiceIntent();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminServiceIntent();
    }
}