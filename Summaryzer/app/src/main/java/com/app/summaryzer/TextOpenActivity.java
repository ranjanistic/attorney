package com.app.summaryzer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TextOpenActivity extends AppCompatActivity {
    TextView filehead, filebody,filepath;
    String fpath;
    NestedScrollView contentScrollView;
    CustomLoadDialogClass whilefilereadload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_text_open);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contentScrollView = findViewById(R.id.contentView);
        if(bundle!=null) {
            fpath = Objects.requireNonNull(bundle).getString("fileUri");
            new loadFileTask().execute(fpath);
        }

        if(bundle==null){
            Intent textintent = getIntent();
            if(textintent.getAction()!=null&&textintent.getAction().equals(Intent.ACTION_VIEW)){
                String scheme = textintent.getScheme();
                if(scheme!=null && scheme.equals(ContentResolver.SCHEME_CONTENT)){
                    Uri uri = textintent.getData();
                    if (uri != null) {
                        getFileName(uri);
                        getFileContent(uri);
                        getFilePath(uri);
                    }
                }
            }
        }

        ImageButton processBtn = findViewById(R.id.processbutt);
        processBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent process = new Intent(TextOpenActivity.this, TextProcessor.class);
               startActivity(process);
            }
        });

        final ImageButton fullscrollbtn = findViewById(R.id.fullScrollButt);
        final View locationbar = findViewById(R.id.locationbottombar);
        if (contentScrollView != null) {
            contentScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == 0) {
                        fullscrollbtn.setVisibility(View.INVISIBLE);
                    }else {
                        fullscrollbtn.setVisibility(View.VISIBLE);
                    }
                    if (scrollY < oldScrollY) {
                        locationbar.animate().alpha(1);
                        fullscrollbtn.animate().rotation(-90);
                        fullscrollbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                contentScrollView.fullScroll(NestedScrollView.FOCUS_UP);

                            }
                        });
                    }
                    if (scrollY > oldScrollY) {
                        locationbar.animate().alpha(0);
                        fullscrollbtn.animate().rotation(90);
                        fullscrollbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                contentScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                            }
                        });
                    }
                }
            });
        }
    }

    public class loadFileTask extends AsyncTask<String,Void,String>{
        @Override
        public void onPreExecute(){
            whilefilereadload = new CustomLoadDialogClass(TextOpenActivity.this, new OnDialogLoadListener() {
                @Override
                public void onLoad() {
                }
                @Override
                public String onLoadText() {
                    return "Loading file";
                }
            });
            whilefilereadload.show();
        }
        @Override
        public String doInBackground(String... pathdata){
            String path = pathdata[0];
            getFileName(Uri.parse(path));
            getFileContent(Uri.parse(path));
            getFilePath(Uri.parse(path));
            return null;
        }
        @Override
        public void onPostExecute(String value){
            super.onPostExecute(value);
            whilefilereadload.dismiss();
        }
    }


    private void getFileContent(Uri uri) {
        InputStream inputStream = null;
        StringBuilder text = new StringBuilder();
        try {
            inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            Log.i("","open text file - content"+"\n");
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        filebody = findViewById(R.id.textfilecontent);
        filebody.setText(text.toString());
    }

    public void getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        filehead = findViewById(R.id.textfilename);
        filehead.setText(result);
    }

    public void getFilePath(Uri uri){
        File file = new File(uri.getPath());//create path from uri
        final String[] split = file.getPath().split(":");//split the path.
        String filePath = split[1];
        filepath = findViewById(R.id.textfilelocation);
        filePath = "at " + filePath;
        filepath.setText(filePath);
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