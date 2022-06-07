package com.example.firstmobileapp.views.admin.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Services extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference services = reference.child("services");
    private final Query servicesQuery = services.orderByChild("service");

    private Button addServiceScreenButton, removeServiceScreenButton;
    private ListView servicesListView;
    private final ArrayList<String> servicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        addServiceScreenButton = findViewById(R.id.addServiceScreenButton);
        removeServiceScreenButton = findViewById(R.id.removeServiceScreenButton);
        servicesListView = findViewById(R.id.servicesList);

        servicesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Service services = data.getValue(Service.class);

                    servicesList.add("Service: " + services.getService() + " | price: " + services.getPrice());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, servicesList);
                    servicesListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Services.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addServiceScreenButton.setOnClickListener(view -> intents.adminServiceAddAIntent());

        removeServiceScreenButton.setOnClickListener(view -> intents.adminServiceRemoveAIntent());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminMainIntent();
    }
}