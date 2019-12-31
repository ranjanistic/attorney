package com.app.summaryzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Register extends AppCompatActivity {

    EditText emailIdReceived, passNewReceived, passCnfReceived;
    ImageButton signupbutt;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String alheading,alsubheading, loginMessage, mail,pass;
    Drawable alertImage;
    CustomAlertDialog netErrorDialog, linkSentAlertDialog;
    CustomLoadDialogClass loadDialogClassWhileRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        signupbutt = findViewById(R.id.signupbtn);
        
        netErrorDialog = new CustomAlertDialog(Register.this, new OnDialogAlertListener() {
            @Override
            public String onCallText() {
                return alheading;
            }

            @Override
            public String onCallSub() {
                return alsubheading;
            }

            @Override
            public Drawable onCallImg() { return alertImage; }
        });
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            signupbutt.setTooltipText("Register now");
        }
        signupbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailIdReceived = findViewById(R.id.emailIDRegisterActivity);
                passNewReceived = findViewById(R.id.passwordNewRegisterActivity);
                passCnfReceived = findViewById(R.id.passwordCnfRegisterActivity);
                
                if(checknet()) {
                    loadDialogClassWhileRegister = new CustomLoadDialogClass(Register.this, new OnDialogLoadListener() {
                        @Override
                        public void onLoad(){
                            pass = passNewReceived.getText().toString();
                            mail = emailIdReceived.getText().toString();
                            if(passNewReceived.getText().toString().equals(passCnfReceived.getText().toString()))
                                pass = passCnfReceived.getText().toString();
                            new regisTask().execute(mail,pass);
                        }
                        @Override
                        public String onLoadText() {
                            return "Registering";
                        }
                    });
                    registerInit();
                } else {
                    alertBox(R.drawable.ic_disconnectiontower,"Network Failure","Error reaching server. Check your internet connection.");
                    netErrorDialog.show();
                }
            }
        });

        linkSentAlertDialog = new CustomAlertDialog(this, new OnDialogAlertListener() {
            @Override
            public String onCallText() {
                return "Verify your Authenticity";
            }

            @Override
            public String onCallSub() {
                return "A link has been sent at "+mail;
            }

            @Override
            public Drawable onCallImg() {
                return getResources().getDrawable(R.drawable.ic_problembadge);
            }
        });
        //anonymous login button listener
        ImageButton anonymlogbtn  = findViewById(R.id.anonymloginbtn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            anonymlogbtn.setTooltipText("Login Anonymously");
        }


        anonymlogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginMessage  = getResources().getString(R.string.anonym_loginInfo);
                Intent confirm =  new Intent(Register.this, ConfirmationActivity.class);
                confirm.putExtra("contTitle", "Continue as");
                confirm.putExtra("contVia", "Anonymous?");
                confirm.putExtra("contText", loginMessage);
                confirm.putExtra("contCode", "a");
                startActivity(confirm);
            }
        });

        //google login button listener
        ImageButton googlelogbtn = findViewById(R.id.googleloginbtn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            googlelogbtn.setTooltipText("Login with Google");
        }

        googlelogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginMessage = getResources().getString(R.string.google_loginInfo);
                Intent confIntent = new Intent(Register.this,ConfirmationActivity.class);
                confIntent.putExtra("contTitle", "Continue with");
                confIntent.putExtra("contVia", "Google?");
                confIntent.putExtra("contText", loginMessage);
                confIntent.putExtra("contCode", "g");
                startActivity(confIntent);
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

    public class regisTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... creds){
            String emailcred = creds[0];
            String passcred = creds[1];
            register(emailcred, passcred);
            return emailcred;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }

    private void registerInit(){
        String email, pass, cpass;
        email = emailIdReceived.getText().toString();
        pass = passNewReceived.getText().toString();
        cpass = passCnfReceived.getText().toString();
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
            loadDialogClassWhileRegister.show();
        }

    }

    private void register(String uid, String passphrase){
        mAuth.createUserWithEmailAndPassword(uid, passphrase)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        if (task.isSuccessful()) {
                            if (checkIfEmailVerified()) {
                                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                linkSentAlertDialog.show();
                                linkSentAlertDialog.setCanceledOnTouchOutside(false);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                            loadDialogClassWhileRegister.dismiss();
                        }
                    }
                });
    }

    private boolean checkIfEmailVerified() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        return Objects.requireNonNull(user).isEmailVerified();
    }

    boolean checknet() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }
    private void alertBox(int aimg, String h, String sh){
        alertImage = getResources().getDrawable(aimg);
        alheading = h;
        alsubheading = sh;
    }

    private void themSetter(int tcode){
        switch (tcode){
            case 101: setTheme(R.style.AppTheme);
                break;
            case 102: setTheme(R.style.LightTheme);
                break;
            case 103: setTheme(R.style.joyTheme);
                break;
        }
    }
    private int getThemeStatus() {
        SharedPreferences mSharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        return mSharedPreferences.getInt("themeCode", 0);
    }
}
