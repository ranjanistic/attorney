package com.app.summaryzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText editMail, editPass, cnfpass;
    ImageButton signup;
    FirebaseAuth mAuth;
    boolean isOpen =true, stopFlag = true, isOpenload = false;
    ImageView loadanim;
    Animation loadOpen, loadClose, loadrot, loadarot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.charcoal));
        window.setNavigationBarColor(this.getResources().getColor(R.color.charcoal));

        mAuth = FirebaseAuth.getInstance();
        editMail = findViewById(R.id.addemailID);

        editPass = findViewById(R.id.newpassword);
        cnfpass = findViewById(R.id.cnfpassword);
        signup = findViewById(R.id.signupbtn);
       // //loadanim = findViewById(R.id.regisload);
        loadrot = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clk);
        loadarot = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_aclk);
        //signup.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            signup.setTooltipText("Register now");
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    register();
            }
        });

        ImageButton anonymlogbtn  = findViewById(R.id.anonymloginbtn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            anonymlogbtn.setTooltipText("Login Anonymously");
        }
        anonymlogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent confirm =  new Intent(Register.this, ConfirmationActivity.class);
                startActivity(confirm);
            }
        });

        ImageButton googlelogbtn = findViewById(R.id.googleloginbtn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           googlelogbtn.setTooltipText("Login with Google");
        }
        googlelogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">Big Ooof</font><bgcolor=\"#000\"</bgcolor>"), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button signin = findViewById(R.id.signinbutt);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginintent = new Intent(Register.this, Login.class);
                startActivity(loginintent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });



    }

    private void register(){
        String email, pass, cpass;
        email = editMail.getText().toString();
        pass = editPass.getText().toString();
        cpass = cnfpass.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;

        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(cpass)) {
            Toast.makeText(getApplicationContext(), "Please confirm password!", Toast.LENGTH_LONG).show();
            return;
        }
        if(!pass.equals(cpass)){
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                isOpen = true;
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();;
                                isOpen = false;
                            }
                        }
                    });
        }

        }
}
