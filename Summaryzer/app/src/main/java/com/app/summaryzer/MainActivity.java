package com.app.summaryzer;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ScrollView;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         final    Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.charcoal));
            window.setNavigationBarColor(this.getResources().getColor(R.color.spruce));
     //   Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageButton accountbtn = findViewById(R.id.accountbtn);
        ImageButton openFIle = findViewById(R.id.fileopenbtn);
        ImageButton openLink = findViewById(R.id.linkopenbtn);
        mauth = FirebaseAuth.getInstance();
        //final SwipeRefreshLayout pullToRefresh = findViewById(R.id.refreshhome);
        //pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //@Override
            //public void onRefresh() {
                // your code
             //   pullToRefresh.setRefreshing(false);
            //}
        //});

        final Intent sumintent = new Intent(MainActivity.this, Summary.class);
        ImageButton go = findViewById(R.id.gobtn);
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

        openFIle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFile.setType("text/plain");
                startActivityForResult(
                        Intent.createChooser(chooseFile, "Choose a text file"), PICKFILE_RESULT_CODE);
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            accountbtn.setTooltipText("Settings");
        }


        accountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accountact = new Intent(MainActivity.this, AccountView.class);
                startActivity(accountact);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK){
            Uri content_describer = data.getData();
            String src = content_describer.getPath();
            String content = content_describer.getSchemeSpecificPart();     //TODO: get content
            String filename = content_describer.getLastPathSegment();
            Intent intent = new Intent(MainActivity.this, TextOpenActivity.class);
            intent.putExtra("filename",filename);
            intent.putExtra("filepath",src);
            intent.putExtra("filecontent", content);
            startActivity(intent);

        }
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

