package com.app.summaryzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Credentials;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.summaryzer.ui.Account.AccountFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class ConfirmationActivity extends AppCompatActivity {
    String caption, loginpath, continueMessage, imageCode;
    TextView cCaption, cLoginPath, cMessage;
    ImageView cBackImg;
    Window window;
    private FirebaseAuth firebaseAuth;
    CustomLoadDialogClass loadingwhilelogin;
    public static int RC_SIGN_IN = 1, AN_SIGN_IN=2;
    GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    String idToken,pname, pEmail;
    Uri pPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        caption = Objects.requireNonNull(bundle).getString("contTitle");
        loginpath = Objects.requireNonNull(bundle).getString("contVia");
        continueMessage = Objects.requireNonNull(bundle).getString("contText");
        imageCode = Objects.requireNonNull(bundle).getString("contCode");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        View activityFull = findViewById(R.id.confirmationActivity);
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        cCaption = findViewById(R.id.continueTitle);
        cLoginPath = findViewById(R.id.continueAs);
        cMessage  = findViewById(R.id.continueInfo);
        cBackImg = findViewById(R.id.continueBackImage);

        cCaption.setText(caption);
        cLoginPath.setText(loginpath);
        cMessage.setText(continueMessage);
        if(imageCode.equals("g")) {
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));
            cMessage.setTypeface(Typeface.DEFAULT_BOLD);
            cCaption.setTextColor(getResources().getColor(R.color.black));
            cLoginPath.setTextColor(getResources().getColor(R.color.black));
            cMessage.setTextColor(getResources().getColor(R.color.black));
            cBackImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_googleglogo));
            activityFull.setBackgroundColor(getResources().getColor(R.color.white));
        }
        else if(imageCode.equals("a")){
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
            window.setNavigationBarColor(this.getResources().getColor(R.color.black));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            cCaption.setTextColor(getResources().getColor(R.color.white));
            cLoginPath.setTextColor(getResources().getColor(R.color.white));
            cMessage.setTextColor(getResources().getColor(R.color.white));
            cBackImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_icognitoman));
            activityFull.setBackgroundColor(getResources().getColor(R.color.black));

            gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))//you can also use R.string.default_web_client_id
                    .requestEmail()
                    .build();
            googleApiClient=new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            t(connectionResult.toString());
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                    .build();

        }
        loadingwhilelogin = new CustomLoadDialogClass(this, new OnDialogLoadListener() {
            @Override
            public void onLoad() {
            }

            @Override
            public String onLoadText() {
                return "Working";
            }
        });

        Button abortlogin = findViewById(R.id.cancelLogin);
        abortlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button contlogin = findViewById(R.id.continueLogin);
        contlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingwhilelogin.show();

                if(imageCode.equals("a")){
                    anonymLogin();
                } else if(imageCode.equals("g")){
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            t("three");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleLogResult(result);
        } else if(requestCode == AN_SIGN_IN){
            anonymLogin();
        }
    }

    private void anonymLogin(){
        t("no symbiosis");
    }


    private void handleGoogleLogResult(GoogleSignInResult result){
        if(result.isSuccess()){
            t("four");
            GoogleSignInAccount account = result.getSignInAccount();
             idToken = account.getIdToken();
            pname = account.getDisplayName();
            pEmail = account.getEmail();
            pPhoto = account.getPhotoUrl();
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
            t("five");
            firebaseAuthWGoogle(credential);
        }   else{
            t("failedfive");
        }
        loadingwhilelogin.dismiss();
    }
    private void firebaseAuthWGoogle(AuthCredential cred){
        firebaseAuth.signInWithCredential(cred)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            t("Symbiosis");
                            backToProfile();
                        } else {
                            task.getException().printStackTrace();
                            t("Can't auth");
                        }
                    }
                });
    }

    private void backToProfile(){
        Intent i = new Intent(ConfirmationActivity.this,AccountView.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("name", pname);
        i.putExtra("mailid", pEmail);
        i.putExtra("photo", pPhoto.toString());
        startActivity(i);
    }
    private void t(String s){
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
    }

}