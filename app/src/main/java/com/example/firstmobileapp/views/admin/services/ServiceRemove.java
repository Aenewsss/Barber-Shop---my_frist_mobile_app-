package com.example.firstmobileapp.views.admin.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceRemove extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference services = reference.child("services");

    private EditText insertServiceNameToRemove;
    private Button removeServiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_remove);

        insertServiceNameToRemove = findViewById(R.id.insertServiceNameToRemove);
        removeServiceButton = findViewById(R.id.removeServiceButton);

        removeServiceButton.setOnClickListener(view -> {
            String serviceToRemove = insertServiceNameToRemove.getText().toString();

            if(TextUtils.isEmpty(serviceToRemove)){
                Toast.makeText(ServiceRemove.this, "Please, fill in all fields", Toast.LENGTH_SHORT).show();
            }else{
                services.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Service service = data.getValue(Service.class);
                            String serviceType = service.getService();

                            if(serviceToRemove.equals(serviceType)){
                                services.child(data.getKey()).removeValue();
                                intents.adminServiceIntent();
                                return;
                            }
                        }
                        String message = "Service " + serviceToRemove + " not exists in DB";
                        Toast.makeText(ServiceRemove.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ServiceRemove.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminServiceIntent();
    }
}