package com.app.summaryzer;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab_menu, fab_report;
    Animation fabOpen, fabClose,fabClk, fabAclk;
    boolean isOpen =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         final    Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
            window.setNavigationBarColor(this.getResources().getColor(R.color.nav_red));

        fab_menu  = findViewById(R.id.fab_menu);
        fab_report = findViewById(R.id.fab_report);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabAclk = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_aclk);
        fabClk = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clk);
        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    fab_report.startAnimation(fabClose);
                    fab_menu.startAnimation(fabAclk);
                    fab_report.setClickable(false);
                    isOpen = false;
                }else{
                    fab_report.startAnimation(fabOpen);
                    fab_menu.startAnimation(fabClk);
                    fab_report.setClickable(true);
                    isOpen = true;
                }
            }
        });
        final Intent sumintent = new Intent(MainActivity.this, Summary.class);
        Button go = findViewById(R.id.gobtn);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this,R.style.DialogTheme);
                View mView = getLayoutInflater().inflate(R.layout.privacy_notice, null);
                CheckBox mCheckBox = mView.findViewById(R.id.checkBox);
                mBuilder.setIcon(R.drawable.ic_launcher_foreground);
                mBuilder.setTitle("Privacy Recommendation Notice");
                mBuilder.setMessage("We respect the privacy of users, and we do not share your content with any third party. Yet we strongly recommend you to not enter any sensitive information here.\nAll data is stored in our database for development purposes.\n\nFor anonymous usage, no data is associated with any person in particular.\nFor signed-in usage, data is associated with the user and is researched for the betterment of personal experience.\n\nYou must agree with this to proceed.");
                mBuilder.setView(mView);
                mBuilder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(sumintent);
                    }
                });
                mBuilder.setNegativeButton("I Agree to disagree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(compoundButton.isChecked()){
                            storeDialogStatus(true);
                        }else{
                            storeDialogStatus(false);
                        }
                    }
                });
                if(getDialogStatus()){
                    mDialog.hide();
                    startActivity(sumintent);
                }else{
                    mDialog.show();
                }
            }
        });

        ImageButton accountbtn = findViewById(R.id.accountbtn);
        accountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registeract = new Intent(MainActivity.this, Register.class);
                startActivity(registeract);
            }
        });
    }
    private void storeDialogStatus(boolean isChecked){
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus(){
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);
    }
}
