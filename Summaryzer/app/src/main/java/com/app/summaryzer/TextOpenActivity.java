package com.app.summaryzer;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TextOpenActivity extends AppCompatActivity {
    String fpath;
    String head, body,location ;
    NestedScrollView contentScrollView;
    CustomLoadDialogClass whilefilereadload;
    String[] recenttext = {"recent0", "recent1", "recent2"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_text_open);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contentScrollView = findViewById(R.id.contentView);
        TextView filehead, filebody,filepath;
        filehead = findViewById(R.id.textfilename);
        filebody = findViewById(R.id.textfilecontent);
        filepath = findViewById(R.id.textfilelocation);
        if(bundle!=null) {
            fpath = Objects.requireNonNull(bundle).getString("fileUri");
            //new loadFileTask().execute(fpath);
            head = getFileName(Uri.parse(fpath));
            body = getFileContent(Uri.parse(fpath));
            location = getFilePath(Uri.parse(fpath));
            storeFileContent(head,body);
            filehead.setText(head);
            filebody.setText(body);
            filepath.setText(location);
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

    private String getFileContent(Uri uri) {
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

        return text.toString();
    }

    public String getFileName(Uri uri) {
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
        return result;
    }

    public String getFilePath(Uri uri){
        File file = new File(uri.getPath());//create path from uri
        final String[] split = file.getPath().split(":");//split the path.
        String filePath = split[1];
        filePath = "at " + filePath;
        return filePath;
    }

    private void storeFileContent(String title, String content){
        int count = 2;
        while(count>0) {
            String[] data = getRecentText(count-1);
            SharedPreferences mSharedPreferences = getSharedPreferences(recenttext[count], MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putString("heading", data[0]);
            mEditor.putString("body", data[1]);
            mEditor.apply();
            count-=1;
        }
        SharedPreferences mSharedPreferences = getSharedPreferences(recenttext[0], MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("heading", title);
        mEditor.putString("body", content);
        mEditor.apply();
    }

    private String[] getRecentText(int recentCode){
        String[] path = {null,null};
        SharedPreferences mSharedPreferences = getSharedPreferences(recenttext[recentCode], MODE_PRIVATE);
        path[0] = mSharedPreferences.getString("heading", "");
        path[1] = mSharedPreferences.getString("body", "");
        return path;
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