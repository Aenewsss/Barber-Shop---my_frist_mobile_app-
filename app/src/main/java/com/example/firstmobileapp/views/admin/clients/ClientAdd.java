package com.example.firstmobileapp.views.admin.clients;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientAdd extends AppCompatActivity {
    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference clients = reference.child("clients");

    private Button addClientButton;
    private EditText usernameAddText, passwordAddText, ageAddText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add);

        addClientButton = findViewById(R.id.add_client_button);

        usernameAddText = findViewById(R.id.username_add);
        passwordAddText = findViewById(R.id.password_add);
        ageAddText = findViewById(R.id.age_add);

        addClientButton.setOnClickListener(view -> {
                String username = usernameAddText.getText().toString(), password = passwordAddText.getText().toString(), age = ageAddText.getText().toString();

//                User user = new User(username, age, email, phone);

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(ClientAdd.this, "Please, fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

//                clients.push().setValue(user);

                intents.adminClientIntent();
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminClientIntent();
    }
}