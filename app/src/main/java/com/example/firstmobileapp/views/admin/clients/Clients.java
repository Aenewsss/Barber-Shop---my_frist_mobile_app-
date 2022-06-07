package com.example.firstmobileapp.views.admin.clients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Clients extends AppCompatActivity {
    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference clients = reference.child("clients");

    private final ArrayList<String> clientsListView = new ArrayList<>();
    private ListView listClients;
    private Button addClientsButton, removeClientButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        addClientsButton = findViewById(R.id.addClientButton);
        removeClientButton = findViewById(R.id.removeClientButton);

        addClientsButton.setOnClickListener(view -> intents.adminClientAddIntent());

        removeClientButton.setOnClickListener(view -> intents.adminClientRemoveIntent());
        // insert values in list
        clients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User clients = data.getValue(User.class);

                    clientsListView.add(0,"Name: " + clients.getUsername() + "| Age: " + clients.getAge());
                }
                listClients = findViewById(R.id.clients_list_view);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1 ,clientsListView);
                listClients.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Clients.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminMainIntent();
    }
}