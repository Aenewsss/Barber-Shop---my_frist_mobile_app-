package com.example.firstmobileapp.views.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.Appointment;
import com.example.firstmobileapp.models.Client;
import com.example.firstmobileapp.models.ScheduleModel;
import com.example.firstmobileapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference dates = reference.child("dates");
    private final DatabaseReference clients = reference.child("clients");

    private ListView listUsers;
    private Spinner spinner;
    private final ArrayList<String> clientsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textName = findViewById(R.id.textName);
        Button returnLoginButton = findViewById(R.id.returnLogin);
        spinner = findViewById(R.id.spinnerMain);

        String date = formatDate();

        // creating and displaying spinner with dropdown
        setSpinner();

        // insert email of logged user
        textName.setText(fAuth.getCurrentUser().getEmail());

        // return to login view
        returnLoginButton.setOnClickListener(view -> intents.loginIntent());

        // check selected item clicked in spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0: // clients
                        intents.adminClientIntent();
                        break;
                    case 1: // services
                        intents.adminServiceIntent();
                        break;
                    case 2: // employees
                        intents.adminEmployeesIntent();
                        break;
                    case 3: // schedules
                        intents.adminScheduleIntent();
                        break;
                    case 4: // settings
                        intents.adminSettingsIntent();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dates.child(date).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                clientsList.clear();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){
            intents.loginIntent();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpinnerPosition();
        fillListClients();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void fillListClients() {
        clientsList.clear();

        String date = formatDate();

        dates.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if(!data.getValue().toString().equals("true")){
                        Client client = data.getValue(Client.class);

                        clients.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot data : snapshot.getChildren()){
                                    User user = data.getValue(User.class);

                                    if(client.getClient().equals(user.getEmail())){
                                        clients.child(data.getKey()).child("appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot data : snapshot.getChildren()){
                                                    if(data.getKey().equals(date)){
                                                      Appointment appointment = data.getValue(Appointment.class);
                                                      clientsList.add(user.getUsername() + " | Time: " + appointment.getTime() + " | Service: " + appointment.getService());
                                                    }
                                                }
                                                if(clientsList.size() == 0) clientsList.add("No customers today");
                                                listUsers = findViewById(R.id.listUsers);
                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, clientsList);
                                                listUsers.setAdapter(adapter);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Main.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Main.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinner(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                Main.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.options)
        );

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        setSpinnerPosition();
    }

    private void setSpinnerPosition(){ spinner.setSelection(4, false); }

    private String formatDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();

        String dateFormatted = formatter.format(todayDate);

        String dateDay = dateFormatted.substring(0,2);
        String dateMonth = dateFormatted.substring(3,5);
        String dateYear = dateFormatted.substring(6, 10);

        if(dateDay.indexOf("0") == 0){
            dateDay = dateDay.substring(1,2);
        }
        if(dateMonth.indexOf("0") == 0){
            dateMonth = dateMonth.substring(1,2);
        }

        return dateDay + "-" + dateMonth + "-" + dateYear;
    }
}