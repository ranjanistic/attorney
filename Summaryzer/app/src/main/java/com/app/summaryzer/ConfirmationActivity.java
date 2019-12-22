package com.app.summaryzer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ConfirmationActivity extends AppCompatActivity {
    String caption, loginpath, continueMessage, imageCode;
    TextView cCaption, cLoginPath, cMessage;
    ImageView cBackImg;
    Window window;
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
            cCaption.setTextColor(getResources().getColor(R.color.black));
            cLoginPath.setTextColor(getResources().getColor(R.color.black));
            cMessage.setTextColor(getResources().getColor(R.color.black));
            cMessage.setTypeface(Typeface.DEFAULT_BOLD);
            cBackImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_google));
            activityFull.setBackgroundColor(getResources().getColor(R.color.white));
        } else if(imageCode.equals("a")){
            window.setStatusBarColor(this.getResources().getColor(R.color.charcoal));
            window.setNavigationBarColor(this.getResources().getColor(R.color.charcoal));
            cCaption.setTextColor(getResources().getColor(R.color.white));
            cLoginPath.setTextColor(getResources().getColor(R.color.white));
            cMessage.setTextColor(getResources().getColor(R.color.white));
            cBackImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_icognitoman));
            activityFull.setBackgroundColor(getResources().getColor(R.color.charcoal));
        }
        Button abortlogin = findViewById(R.id.cancelLogin);
        abortlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}