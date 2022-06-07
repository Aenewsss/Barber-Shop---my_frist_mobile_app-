package com.example.firstmobileapp.middlewares;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstmobileapp.views.client.Account;
import com.example.firstmobileapp.views.client.Appointments;
import com.example.firstmobileapp.views.client.Main;
import com.example.firstmobileapp.views.Login;
import com.example.firstmobileapp.views.Register;
import com.example.firstmobileapp.views.admin.clients.ClientAdd;
import com.example.firstmobileapp.views.admin.clients.ClientRemove;
import com.example.firstmobileapp.views.admin.clients.Clients;
import com.example.firstmobileapp.views.admin.employees.Employees;
import com.example.firstmobileapp.views.admin.schedule.Schedule;
import com.example.firstmobileapp.views.admin.schedule.ScheduleAdd;
import com.example.firstmobileapp.views.admin.schedule.ScheduleRemove;
import com.example.firstmobileapp.views.admin.services.ServiceAdd;
import com.example.firstmobileapp.views.admin.services.ServiceRemove;
import com.example.firstmobileapp.views.admin.services.Services;
import com.example.firstmobileapp.views.admin.settings.Settings;
import com.example.firstmobileapp.views.client.Mark;
import com.google.firebase.auth.FirebaseAuth;

public class Intents extends AppCompatActivity {

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final Context context;

    public Intents(Context context) {
        this.context = context;
    }

    //------------- LOGIN -------------//
    public void loginIntent(){
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
        finish();
    }

    //------------- REGISTER -------------//
    public void registerIntent() {
        Intent intent = new Intent(context, Register.class);
        context.startActivity(intent);
        finish();
    }

    //------------- MAIN ACTIVITY ADMIN -------------//
    public void adminMainIntent() {
        Intent intent = new Intent(context, com.example.firstmobileapp.views.admin.Main.class);
        context.startActivity(intent);
        finish();
    }

    //------------- CLIENTS ADMIN -------------//
    public void adminClientIntent(){
        Intent intent = new Intent(context, Clients.class);
        context.startActivity(intent);
        finish();
    }
    public void adminClientAddIntent(){
        Intent intent = new Intent(context, ClientAdd.class);
        context.startActivity(intent);
        finish();
    }
    public void adminClientRemoveIntent(){
        Intent intent = new Intent(context, ClientRemove.class);
        context.startActivity(intent);
        finish();
    }

    //------------- SERVICES ADMIN -------------//
    public void adminServiceIntent(){
        Intent intent = new Intent(context, Services.class);
        context.startActivity(intent);
        finish();
    }
    public void adminServiceAddAIntent(){
        Intent intent = new Intent(context, ServiceAdd.class);
        context.startActivity(intent);
        finish();
    }
    public void adminServiceRemoveAIntent(){
        Intent intent = new Intent(context, ServiceRemove.class);
        context.startActivity(intent);
        finish();
    }

    //------------- SCHEDULES ADMIN -------------//
    public void adminScheduleIntent(){
        Intent intent = new Intent(context, Schedule.class);
        context.startActivity(intent);
        finish();
    }
    public void adminScheduleAddIntent(){
        Intent intent = new Intent(context, ScheduleAdd.class);
        context.startActivity(intent);
        finish();
    }
    public void adminScheduleRemoveIntent(){
        Intent intent = new Intent(context, ScheduleRemove.class);
        context.startActivity(intent);
        finish();
    }

    //------------- SETTINGS ADMIN -------------//
    public void adminSettingsIntent(){
        Intent intent = new Intent(context, Settings.class);
        context.startActivity(intent);
        finish();
    }


    //------------- EMPLOYEES ADMIN -------------//
    public void adminEmployeesIntent(){
        Intent intent = new Intent(context, Employees.class);
        context.startActivity(intent);
        finish();
    }


    //------------- MAIN ACTIVITY CLIENT -------------//
    public void clientMainIntent(){
        Intent intent = new Intent(context, Main.class);
        context.startActivity(intent);
        finish();
    }

    //------------- ACCOUNT CLIENT -------------//
    public void clientAccountIntent(){
        Intent intent = new Intent(context, Account.class);
        context.startActivity(intent);
        finish();
    }

    //------------- APPOINTMENT CLIENT -------------//
    public void clientAppointmentIntent(){
        Intent intent = new Intent(context, Appointments.class);
        context.startActivity(intent);
        finish();
    }

    //------------- MARK CLIENT -------------//
    public void clientMarkIntent(){
        Intent intent = new Intent(context, Mark.class);
        context.startActivity(intent);
        finish();
    }
}
