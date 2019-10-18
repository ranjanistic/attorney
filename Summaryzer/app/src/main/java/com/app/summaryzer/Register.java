package com.app.summaryzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText editMail, editPass;
    ImageButton signup;
    FirebaseAuth mAuth;
    boolean isOpen =true;

    Animation loadOpen, loadClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        window.setNavigationBarColor(this.getResources().getColor(R.color.nav_red));

        mAuth = FirebaseAuth.getInstance();
        final ProgressBar regload = findViewById(R.id.regisload);
        editMail = findViewById(R.id.addemailID);
        editPass = findViewById(R.id.newpassword);
        signup = findViewById(R.id.signupbtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //regload.setVisibility(View.VISIBLE);
                //signup.setVisibility(View.INVISIBLE);
                register();
            }
        });

/*                    Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">Database error Oof</font>"), Snackbar.LENGTH_LONG)
                                .setActionTextColor(getResources().getColor(R.color.white))
                                .setAction("Action", null).show();
                        Toast.makeText(Register.this,"Registered!Not", Toast.LENGTH_LONG).show();
*/
        ImageButton anonymlogbtn  = findViewById(R.id.anonymloginbtn);
        anonymlogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent confirm =  new Intent(Register.this, ConfirmationActivity.class);
                startActivity(confirm);
            }
        });

        ImageButton googlelogbtn = findViewById(R.id.googleloginbtn);
        googlelogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">Big Ooof</font><bgcolor=\"#000\"</bgcolor>"), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //loadOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        //loadClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        final Button signin = findViewById(R.id.signinbutt);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginintent = new Intent(Register.this, Login.class);
                startActivity(loginintent);
                finish();
            }
        });



    }

    private void register(){
        String email, pass;
        email = editMail.getText().toString();
        pass = editPass.getText().toString();
        final ProgressBar regiload = findViewById(R.id.regisload);
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            //regiload.setVisibility(View.INVISIBLE);
            //signup.setVisibility(View.VISIBLE);
            //isOpen= false;
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
         //   regiload.setVisibility(View.INVISIBLE);
            //signup.setVisibility(View.VISIBLE);
            //isOpen = false;
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            isOpen = true;
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                     //       regiload.setVisibility(View.INVISIBLE);
                          //  signup.setVisibility(View.VISIBLE);
                            isOpen = false;
                        }
                    }
                });
    }
}
