package com.example.firstmobileapp.views.admin.clients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClientRemove extends AppCompatActivity {
    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference clients = reference.child("clients");

    private EditText insertUsername;
    private Button removeClientButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_remove);

        insertUsername = findViewById(R.id.removeClientText);

        removeClientButton = findViewById(R.id.remove_client_button);

        removeClientButton.setOnClickListener(view -> {
            String usernameToRemove =  insertUsername.getText().toString();

            if(TextUtils.isEmpty(usernameToRemove)){
                Toast.makeText(ClientRemove.this, "Please, fill in all fields", Toast.LENGTH_SHORT).show();
            }else{
                clients.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            User user = data.getValue(User.class);
                            String username = user.getUsername();

                            if (usernameToRemove.equals(username)) {
                                String key = data.getKey();
                                clients.child(key).removeValue();
                                intents.adminClientIntent();
                                return;
                            }
                        }
                        String message = "User " + usernameToRemove + " not exists in DB";
                        Toast.makeText(ClientRemove.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ClientRemove.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.adminClientIntent();
    }
}