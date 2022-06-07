package com.example.firstmobileapp.views.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.Service;
import com.example.firstmobileapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Mark extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference dates = reference.child("dates");
    private final DatabaseReference services = reference.child("services");
    private final DatabaseReference clients = reference.child("clients");

    private final ArrayList<String> schList = new ArrayList<>();
    private final ArrayList<String> serviceList = new ArrayList<>();
    private String date, spinnerText, spinnerServiceText;
    private int schCount;
    private int serviceCount;

    private Spinner spinnerHour, spinnerService;
    private TextView textDate;
    private Button markButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        spinnerHour = findViewById(R.id.spinnerHour);
        spinnerService = findViewById(R.id.spinnerService);
        textDate = findViewById(R.id.textDate);
        markButton = findViewById(R.id.markButton);

        date = getIntent().getStringExtra("date");

        setTextDate();
        fillSpinnerSch();
        fillSpinnerService();

        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(!adapterView.getSelectedItem().toString().equals("Choose a service")){
                    spinnerServiceText = adapterView.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {  }
        });
        spinnerHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(!adapterView.getSelectedItem().toString().equals("Choose a time")){
                    spinnerText = adapterView.getSelectedItem().toString().replace("h", "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {  }
        });
        markButton.setOnClickListener(view -> {
            if(spinnerText == null || spinnerServiceText == null){
                Toast.makeText(Mark.this, "Please, choose a time and a service", Toast.LENGTH_SHORT).show();
            }else{
                dates.child(date).child(spinnerText).child("client").setValue(fAuth.getCurrentUser().getEmail());
                new Handler().postDelayed(() -> enterAppointmentClientDb(date, spinnerText, spinnerServiceText), 2000);
                intents.clientMainIntent();
                Toast.makeText(Mark.this, "Appointment scheduled", Toast.LENGTH_SHORT).show();
            }
        });
        dates.child(date).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { return; }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Toast.makeText(Mark.this, "The times have changed", Toast.LENGTH_SHORT).show();
                schCount = 0;
                schList.clear();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { return; }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Mark.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
        });
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    User client = data.getValue(User.class);

                    if(client.getEmail().equals(fAuth.getCurrentUser().getEmail())){
                        clients.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("appointments").hasChild(date)){
                                    Toast.makeText(Mark.this, "You already have an appointment today", Toast.LENGTH_SHORT).show();
                                }else{
                                    markButton.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Mark.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Mark.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
        });
    }

    private void enterAppointmentClientDb(String date, String time, String service) {
        clients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    User client = data.getValue(User.class);

                    if(client.getEmail().equals(fAuth.getCurrentUser().getEmail())){
                        clients.child(data.getKey()).child("appointments").child(date).child("time").setValue(time);
                        clients.child(data.getKey()).child("appointments").child(date).child("service").setValue(service);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Mark.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
        });
    }

    private void fillSpinnerService() {
        serviceList.clear();
        services.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Service service = data.getValue(Service.class);

                    String serviceType = service.getService();

                    serviceList.add(serviceType);
                    serviceCount++;
                }
                serviceList.add("Choose a service");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Mark.this, android.R.layout.simple_spinner_item, serviceList);
                adapter.setDropDownViewResource(R.layout.spinner_customized);
                spinnerService.setAdapter(adapter);
                setSpinnerServicePos(serviceCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(Mark.this, error.getMessage(), Toast.LENGTH_SHORT).show(); }
        });
    }

    private void fillSpinnerSch(){
        schList.clear();
        dates.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    if(data.getValue().toString().equals("true")){
                        String time = data.getKey();
                        String text = time + "h";
                        schList.add(text);
                        schCount++;
                    }
                }
                if(schCount == 0) Toast.makeText(Mark.this, "Busy schedule", Toast.LENGTH_SHORT).show();
                schList.add("Choose a time");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Mark.this, android.R.layout.simple_spinner_item, schList);
                adapter.setDropDownViewResource(R.layout.spinner_customized);
                spinnerHour.setAdapter(adapter);
                setSpinnerHourPos(schCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Mark.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setTextDate(){
        String dateConvert = getIntent().getStringExtra("dateConvert");
        textDate.setText(dateConvert);
    }

    private void setSpinnerHourPos(int pos){
        spinnerHour.setSelection(pos, false);
    }

    private void setSpinnerServicePos(int pos){
        spinnerService.setSelection(pos, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intents.clientMainIntent();
    }
}