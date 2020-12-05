package org.ranjanistic.attorney;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.app.summaryzer.R;

import org.ranjanistic.attorney.dialog.CustomConfirmDialogClass;
import org.ranjanistic.attorney.dialog.CustomLoadDialogClass;
import org.ranjanistic.attorney.dialog.CustomProgressDialog;
import org.ranjanistic.attorney.dialog.CustomTextDialog;
import org.ranjanistic.attorney.listener.OnDialogConfirmListener;
import org.ranjanistic.attorney.listener.OnDialogTextListener;
import org.ranjanistic.attorney.listener.OnProgressLoadListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 0;
    private FirebaseAuth mauth;
    CardView cardView1, cardView2, cardView3;
    ProgressBar progressBar;
    CustomProgressDialog customProgressDialog;
    CustomConfirmDialogClass permitDialog;
    CustomLoadDialogClass loadDialogWhileFileChoose;
    ImageButton accountbtn,openFIle,openLink, homescrollbtn, camerabtn;
    String fileLink, linkDhead, linkDsubhead, linkDpos, linkDneg, loadingtxt, linkDhint;
    Drawable linkDimg;
    AppBarLayout appBarLayout;
    String[] recentData = {"",""};
    private String[] storageReadPermission =  {Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_main);

        final TextView rhead0 = findViewById(R.id.recentHead1);
        final TextView rbody0 = findViewById(R.id.recentBody1);
        final TextView rhead1 = findViewById(R.id.recentHead2);
        final TextView rbody1 = findViewById(R.id.recentBody2);
        final TextView rhead2 = findViewById(R.id.recentHead3);
        final TextView rbody2 = findViewById(R.id.recentBody3);
        appBarLayout = findViewById(R.id.homeapp_bar);
        accountbtn = findViewById(R.id.accountbtn);
        openFIle = findViewById(R.id.fileopenbtn);
        openLink = findViewById(R.id.linkopenbtn);
        camerabtn = findViewById(R.id.cambtn);
        progressBar = findViewById(R.id.loadingprogress);
        mauth = FirebaseAuth.getInstance();
        homescrollbtn = findViewById(R.id.homescrollbutt);
        homescrollbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                      appBarLayout.setExpanded(false,true);
                    }
                });
            }
        });
        final String[] fullbody = {"","",""};
        int getcount = 0;
        String bpart;
        while(getcount<3) {
            recentData = getRecentText(getcount);
            if(recentData!=null){
                switch (getcount){
                    case 0:rhead0.setText(recentData[0]);
                    fullbody[0] = recentData[1];
                        if(recentData[1].length()>100) {
                            rbody0.setText(recentData[1].toCharArray(), 0, 100);
                            bpart = rbody0.getText().toString() + "...";
                            rbody0.setText(bpart);
                        } else {
                            rbody0.setText(recentData[1]);
                        }
                        break;
                    case 1: rhead1.setText(recentData[0]);
                    fullbody[1] = recentData[1];
                    if(recentData[1].length()>100){
                        rbody1.setText(recentData[1].toCharArray(),0,100);
                        bpart = rbody1.getText().toString() + "...";
                        rbody1.setText(bpart); }
                    else {
                        rbody1.setText(recentData[1]);
                    }
                    break;
                    case 2: rhead2.setText(recentData[0]);
                    fullbody[2] = recentData[1];
                    if(recentData[1].length()>100){
                        rbody2.setText(recentData[1].toCharArray(),0,100);
                        bpart = rbody2.getText().toString() + "...";
                        rbody2.setText(bpart);
                    } else {
                        rbody2.setText(recentData[1]);
                    }
                    break;
                }
            } else continue;
            getcount+=1;
        }
        cardView1 = findViewById(R.id.recent1);
        cardView1.setOnClickListener(view -> {
            setRecentDataToOpen(rhead0.getText().toString(), fullbody[0]);
            Intent i = new Intent(MainActivity.this,TextOpenActivity.class);
            startActivity(i);
        });
        cardView2 = findViewById(R.id.recent2);
        cardView2.setOnClickListener(view -> {
            setRecentDataToOpen(rhead1.getText().toString(), fullbody[1]);
            Intent i = new Intent(MainActivity.this,TextOpenActivity.class);
            startActivity(i);
        });
        cardView3 = findViewById(R.id.recent3);
        cardView3.setOnClickListener(view -> {
            setRecentDataToOpen(rhead2.getText().toString(), fullbody[2]);
            Intent i = new Intent(MainActivity.this,TextOpenActivity.class);
            startActivity(i);
        });

        camerabtn.setOnClickListener(view -> {
            Intent cam = new Intent(MainActivity.this,CameraActivity.class);
            startActivity(cam);
        });
        customProgressDialog = new CustomProgressDialog(this, new OnProgressLoadListener() {
            @Override
            public void onStart() {
            }
        });
        final CustomTextDialog pastelinkDialog = new CustomTextDialog(this, new OnDialogTextListener() {
            @Override
            public void onApply(String text) {
                fileLink = text;
                new linkTextGenTask().execute(fileLink);
                customProgressDialog.show();
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

        final Intent sumintent = new Intent(MainActivity.this, TextClipboard.class);
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
                overridePendingTransition(R.anim.zoom_in,R.anim.left_out);
            }
        });
    }

    public class chooseFileTask extends AsyncTask<Void,Void, String> {
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
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent resData){
        super.onActivityResult(reqCode,resCode,null);
        if (resCode == Activity.RESULT_OK) {
            Uri uri ;
            if (resData != null) {
                uri = resData.getData();
                if (uri != null) {
                    passFile(uri);
                    Toast.makeText(getApplicationContext(),"passed uri", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "urinull", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Data empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void passFile(Uri uri){
        String UriString = uri.toString();
        Intent viewintent = new Intent(MainActivity.this, TextOpenActivity.class);
        viewintent.putExtra("fileUri", UriString);
        startActivity(viewintent);
    }

    private String[] getRecentText(int recentCode){
        String[] recenttext = {"recent0", "recent1", "recent2"};
        String[] path = {null,null};
        SharedPreferences mSharedPreferences = getSharedPreferences(recenttext[recentCode], MODE_PRIVATE);
        path[0] = mSharedPreferences.getString("heading", "");
        path[1] = mSharedPreferences.getString("body", "");
        return path;
    }
    private void setRecentDataToOpen(String head, String body){
        SharedPreferences mSharedPreferences = getSharedPreferences("recentviewdata", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("heading", head);
        mEditor.putString("body", body);
        mEditor.apply();
    }



    private class linkTextGenTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected String doInBackground(String... hyperlink){
            String link  = hyperlink[0];
            StringBuilder text = new StringBuilder();
            try {
                URL url = new URL(link); //My text file location
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(60000); // timing out in a minute
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String str;
                int prog = 0;
                while ((str = in.readLine()) != null) {
                    text.append(str);
                    prog++;
                    publishProgress(prog);
                }
                in.close();
                return text.toString();
            } catch (Exception e) {
                Log.d("MyTag",e.toString());
                return "";
            }
        }
        @TargetApi(Build.VERSION_CODES.P)
        @Override
        protected void onProgressUpdate(Integer... progress){
            progressBar.setProgress(progress[0], true);
        }
        @Override
        protected void onPostExecute(String result){
            if(!result.equals("")) {
                setRecentDataToOpen("Link file", result);
                Intent intent = new Intent(MainActivity.this, TextOpenActivity.class);
                startActivity(intent);
                super.onPostExecute(result);
            } else {
                customProgressDialog.hide();
            }
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

