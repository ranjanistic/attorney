package com.app.summaryzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;



import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailid, passlogin;
    ImageButton loginBtn;


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.charcoal));
        window.setNavigationBarColor(this.getResources().getColor(R.color.charcoal));
        mAuth = FirebaseAuth.getInstance();

        emailid = findViewById(R.id.emailID);
        passlogin = findViewById(R.id.password);
        loginBtn = findViewById(R.id.signinbtn);
        String email,password;
        password = passlogin.getText().toString();
        email = emailid.getText().toString();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        Button regbutt = findViewById(R.id.registButt);
        regbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser() {

        final String email, password;
        email = emailid.getText().toString();
        password = passlogin.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "We need an Email ID", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "We need the password to continue", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Logged in as "+email, Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! An error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
