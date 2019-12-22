package com.app.summaryzer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TextOpenActivity extends AppCompatActivity {
    TextView filehead;
    TextView filebody;
    String fname,fpath,fcontent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        fname = Objects.requireNonNull(bundle).getString("filename");
        fpath = Objects.requireNonNull(bundle).getString("filepath");
       fcontent = Objects.requireNonNull(bundle).getString("filecontent");
        setContentView(R.layout.activity_text_open);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.charcoal));
        window.setNavigationBarColor(this.getResources().getColor(R.color.spruce));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Snackbar.make(view, "To be added later", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        String rawdata = "";
        filehead = findViewById(R.id.textfilename);
        filebody = findViewById(R.id.textfilecontent);
        //filepath = findViewById(R.id.textfilelocation)

        filehead.setText(fname);
        filebody.setText(fcontent);
/*
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = this.getResources().openRawResource(R.raw.txt);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        if(inputStream!=null){
            try{
                while((rawdata = bufferedReader.readLine())!=null){
                    stringBuffer.append(rawdata);
                }
                filebody.setText(stringBuffer);
                inputStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    */
    }

}