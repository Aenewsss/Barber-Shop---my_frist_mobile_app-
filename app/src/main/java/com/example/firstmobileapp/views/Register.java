package com.example.firstmobileapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.example.firstmobileapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference clients = reference.child("clients");
    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();

    private EditText inputEmail, inputUsername, inputAge, inputPhone, inputPassword, inputRepeatPassword;
    private Button registerButton, loginButton;
    private CheckBox showPasswordCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputRepeatPassword = findViewById(R.id.inputRepeatPassword);
        inputUsername = findViewById(R.id.inputUsername);
        inputAge = findViewById(R.id.inputAge);
        inputPhone = findViewById(R.id.inputPhone);
        registerButton = findViewById(R.id.registerButton2);
        loginButton = findViewById(R.id.loginButton);
        showPasswordCheckBox = findViewById(R.id.showPassword);

        loginButton.setOnClickListener(view -> intents.loginIntent() );

        registerButton.setOnClickListener(view -> {
            String email = inputEmail.getText().toString(),
                    password = inputPassword.getText().toString(),
                    repeatPassword = inputRepeatPassword.getText().toString(),
                    username = inputUsername.getText().toString(),
                    phone = inputPhone.getText().toString(),
                    age = inputAge.getText().toString();


            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword) || TextUtils.isEmpty(username) || TextUtils.isEmpty(age) || TextUtils.isEmpty(phone)){
                Toast.makeText(Register.this, "Please, fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if(username.length() < 3){
                Toast.makeText(Register.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
                return;
            }

            if(phone.length() == 9){
                Toast.makeText(Register.this, "Please, enter DDD in your phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(repeatPassword)) {
                Toast.makeText(Register.this, "Passwords must be equals", Toast.LENGTH_SHORT).show();
            } else {
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveDb(username, age, email, phone);
                        intents.clientMainIntent();
                    } else {
                        Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        showPasswordCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                inputRepeatPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                inputRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    private void saveDb(String username, String age, String email, String phone){
        User client = new User(username, age, email, phone, null);
        clients.push().setValue(client);
    }
}