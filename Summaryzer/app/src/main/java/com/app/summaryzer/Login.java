package com.app.summaryzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;


import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailIdReceived, passwordReceived;
    ImageButton loginBtn;
    Button passResetBtn;
    CustomLoadDialogClass loadDialogClassWhileLogin, loadDialogClassWhileReset;
    CustomTextDialog passwordResetDialog;
    private FirebaseAuth mAuth;
    private static String emailforlogintask = "", passwordforlogintask = "";
    String alheading,alsubheading, resetEmail, textDhead, textDsubhead, textsubmit, textCancel, loginMessage;
    Drawable alertImage, textDimg;
    CustomAlertDialog netErrorDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.charcoal));
        window.setNavigationBarColor(this.getResources().getColor(R.color.charcoal));

        passResetBtn =  findViewById(R.id.forgotpassbtn);
        mAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.signinbtn);

        netErrorDialog = new CustomAlertDialog(Login.this, new OnDialogAlertListener() {
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

        loadDialogClassWhileReset = new CustomLoadDialogClass(this, new OnDialogLoadListener() {
            @Override
            public void onLoad() {
                new passResetTask().execute(resetEmail);
            }

            @Override
            public String onLoadText() {
                return "Sending the link";
            }
        });
        passwordResetDialog = new CustomTextDialog(this, new OnDialogTextListener() {
            @Override
            public void onApply(String text) {
                resetEmail = text;
                loadDialogClassWhileReset.show();
            }

            @Override
            public String onCallText() {
                return textDhead;
            }

            @Override
            public String onCallSubText() {
                return textDsubhead;
            }

            @Override
            public String onCallPos() {
                return textsubmit;
            }

            @Override
            public String onCallNeg() {
                return textCancel;
            }

            @Override
            public Drawable onCallImg() {
                return textDimg;
            }
        });
        //login button listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailIdReceived = findViewById(R.id.emailIDLoginActivity);
                passwordReceived = findViewById(R.id.passwordloginactivity);
                if (checknet()) {
                    loadDialogClassWhileLogin = new CustomLoadDialogClass(Login.this, new OnDialogLoadListener() {
                        @Override
                        public void onLoad()
                        {
                            new loginTask().execute(emailforlogintask,passwordforlogintask);
                        }
                        @Override
                        public String onLoadText() {
                            return "Verifying \'"+emailforlogintask+"\'";
                        }
                    });
                    loginUserInit();        //initial credential check before actual login
                } else{
                    alertBox(R.drawable.ic_bug,"Network Failure","Error reaching server. Check your internet connection.");
                    netErrorDialog.show();
                }
            }
        });

        //forgot password listener
        passResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checknet()) {
                    editTextBox(R.drawable.ic_bug, "Password reset", "Enter your email ID to receive a temporary password reset link..", "Send Link", "Abort");
                    passwordResetDialog.show();
                } else{
                    alertBox(R.drawable.ic_bug,"Network Failure","Error reaching server. Check your internet connection.");
                    netErrorDialog.show();
                }
            }
        });

        //register button listener
        Button regbutt = findViewById(R.id.registButt);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            regbutt.setTooltipText("Sign in now");
        }
        regbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
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
                Intent confirm =  new Intent(Login.this, ConfirmationActivity.class);
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
                Intent confIntent = new Intent(Login.this,ConfirmationActivity.class);
                confIntent.putExtra("contTitle", "Continue with");
                confIntent.putExtra("contVia", "Google?");
                confIntent.putExtra("contText", loginMessage);
                confIntent.putExtra("contCode", "g");
                startActivity(confIntent);
            }
        });

    }
    public class loginTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... creds){
            String emailcred = creds[0];
            String passcred = creds[1];
            loginUser(emailcred, passcred);
            return emailcred;
        }
        @Override
        protected void onPostExecute(String result){
             super.onPostExecute(result);
            }
    }
    public class passResetTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... email){
            String emailDestination = email[0];
            resetPassword(emailDestination);
            return emailDestination;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

        }
    }

    private void loginUserInit() {
        emailforlogintask = emailIdReceived.getText().toString();
        passwordforlogintask = passwordReceived.getText().toString();

        if (TextUtils.isEmpty(emailforlogintask)) {
            Toast.makeText(getApplicationContext(), "We need an Email ID", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(passwordforlogintask)) {
            Toast.makeText(getApplicationContext(), "We need the password to continue", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            loadDialogClassWhileLogin.show();
        }
    }
    private void loginUser(final String emailIdFinalLogin, String passwordFinalLogin){
        mAuth.signInWithEmailAndPassword(emailIdFinalLogin, passwordFinalLogin)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Logged in as "+emailIdFinalLogin, Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! An error occurred", Toast.LENGTH_LONG).show();
                            loadDialogClassWhileLogin.dismiss();
                        }
                    }
                });
    }

    private void resetPassword(String mailIdForReset){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(mailIdForReset)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Success", "Email sent.");
                            Toast.makeText(getApplicationContext(), "Link sent!", Toast.LENGTH_LONG).show();
                            loadDialogClassWhileReset.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error link not sent", Toast.LENGTH_LONG).show();
                            loadDialogClassWhileReset.dismiss();
                        }
                        }
                });
    }

    private void editTextBox(int timg, String h, String sh, String tsubmit, String tcancel){
        textDimg = getResources().getDrawable(timg);
        textDhead = h;
        textDsubhead = sh;
        textsubmit = tsubmit;
        textCancel = tcancel;
    }

    private void alertBox(int aimg, String h, String sh){
        alertImage = getResources().getDrawable(aimg);
        alheading = h;
        alsubheading = sh;
    }

    boolean checknet() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else
            connected = false;
        return connected;
    }
}
