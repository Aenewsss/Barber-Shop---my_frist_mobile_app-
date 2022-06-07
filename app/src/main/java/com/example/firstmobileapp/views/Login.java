package com.example.firstmobileapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstmobileapp.R;
import com.example.firstmobileapp.middlewares.Intents;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private final Intents intents = new Intents(this);

    private FirebaseAuth fAuth;

    private Button loginButton, registerButton, forgotPasswordButton;
    private EditText inputEmail, inputPassword;
    private CheckBox showPasswordCheckBox;
    private String admin_email;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        showPasswordCheckBox = findViewById(R.id.showPassword);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        admin_email= getResources().getString(R.string.admin_email);

        loginButton.setOnClickListener(view -> {
            String email = inputEmail.getText().toString(), password = inputPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "User or password invalid", Toast.LENGTH_SHORT).show();
                return;
            }

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    count++;
                    if(count >= 3){
                        forgotPasswordButton.setVisibility(View.VISIBLE);
                        Toast.makeText(Login.this, "User or password invalid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(Login.this, "User or password invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyActivity(email);
            });
        });

        registerButton.setOnClickListener(view -> { intents.registerIntent(); });

        showPasswordCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        forgotPasswordButton.setOnClickListener(view -> {
            String email = inputEmail.getText().toString();
            fAuth.sendPasswordResetEmail(email);
            Toast.makeText(Login.this, "A new password has been sent to your email", Toast.LENGTH_SHORT).show();
        });
    }

    private void verifyActivity(String email){
        if(email.equals(admin_email)){
            intents.adminMainIntent();
        }else{
            intents.clientMainIntent();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}