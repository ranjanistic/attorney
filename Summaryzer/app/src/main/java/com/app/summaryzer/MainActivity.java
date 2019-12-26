package com.app.summaryzer;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
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
import android.widget.Toast;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 0;
    private FirebaseAuth mauth;
    CustomConfirmDialogClass permitDialog;
    CustomLoadDialogClass loadDialogWhileFileChoose;
    ImageButton accountbtn;
    ImageButton openFIle;
    ImageButton openLink;
    String fileLink, linkDhead, linkDsubhead, linkDpos, linkDneg, loadingtxt, linkDhint;
    Drawable linkDimg;
    private String[] storageReadPermission =  {Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_main);

        accountbtn = findViewById(R.id.accountbtn);
        openFIle = findViewById(R.id.fileopenbtn);
        openLink = findViewById(R.id.linkopenbtn);
        mauth = FirebaseAuth.getInstance();

        final CustomTextDialog pastelinkDialog = new CustomTextDialog(this, new OnDialogTextListener() {
            @Override
            public void onApply(String text) {
                fileLink = text;
            }

            @Override
            public String onCallText() {
                return linkDhead;
            }

            @Override
            public String onCallSubText() {
                return linkDsubhead;
            }

            @Override
            public String onCallPos() {
                return linkDpos;
            }

            @Override
            public String onCallNeg() {
                return linkDneg;
            }
            @Override
            public String onCallHint(){ return linkDhint;}
            @Override
            public Drawable onCallImg() {
                return linkDimg;
            }
        });

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

        permitDialog = new CustomConfirmDialogClass(this, new OnDialogConfirmListener() {
            @Override
            public void onApply(Boolean confirm) {
                requestPermissions(storageReadPermission,REQUEST_CODE_PERMISSIONS);
            }

            @Override
            public String onCallText() {
                return "Permission required";
            }

            @Override
            public String onCallSub() {
                return "Storage access permission is required to access text files.";
            }

            @Override
            public String onCallPos() {
                return "Okay, I'll permit";
            }

            @Override
            public String onCallNeg() {
                return "I can't permit";
            }

            @Override
            public Drawable onCallImg() {
                return getResources().getDrawable(R.drawable.ic_icognitoman);
            }
        });



        openFIle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    permitDialog.show();
                    permitDialog.setCanceledOnTouchOutside(false);

                } else {
                    new chooseFileTask().execute();
                }
            }
        });

        openLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkDhead = "Paste link here";
                linkDsubhead = "txt, doc, docx extensions supported.";
                linkDpos = "Next";
                linkDneg = "Cancel";
                linkDhint = "Hyperlink";
                linkDimg = getResources().getDrawable(R.drawable.ic_linkico);
                pastelinkDialog.show();
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

    public class chooseFileTask extends AsyncTask<Void,Void, String> {
        @Override
        public void onPreExecute(){
            loadDialogWhileFileChoose = new CustomLoadDialogClass(MainActivity.this, new OnDialogLoadListener() {
                @Override
                public void onLoad() {
                }
                @Override
                public String onLoadText() {
                    return "Loading data";
                }
            });
            loadDialogWhileFileChoose.show();
        }
        @Override
        public String doInBackground(Void... v){
            //Toast.makeText(MainActivity.this,"loading", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");
            startActivityForResult(intent,PICKFILE_RESULT_CODE);
//            Toast.makeText(getApplicationContext(), "Choose text file", Toast.LENGTH_SHORT).show();
            return null;
        }
        @Override
        public void onPostExecute(String value) {
            super.onPostExecute(value);
            loadDialogWhileFileChoose.dismiss();
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent resData){
        super.onActivityResult(reqCode,resCode,null);
        if (resCode == Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(),"result ok", Toast.LENGTH_SHORT).show();
            Uri uri = null;
            if (resData != null) {
                uri = resData.getData();
                Toast.makeText(getApplicationContext(),"resdata getdata", Toast.LENGTH_SHORT).show();
                passFile(uri);
                Toast.makeText(getApplicationContext(),"passed uri", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Data empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void passFile(Uri uri){
        String UriString = uri.toString();
            Intent viewintent = new Intent(MainActivity.this, TextOpenActivity.class);
        Toast.makeText(getApplicationContext(),"put extra uri string", Toast.LENGTH_SHORT).show();
            viewintent.putExtra("fileUri", UriString);
            startActivity(viewintent);
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

    private void themSetter(int tcode){
        switch (tcode){
            case 101: setTheme(R.style.AppTheme);break;
            case 102: setTheme(R.style.LightTheme);break;
            case 103: setTheme(R.style.joyTheme);break;
        }
    }
    private int getThemeStatus(){
        SharedPreferences mSharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        return mSharedPreferences.getInt("themeCode", 0);
    }
}

