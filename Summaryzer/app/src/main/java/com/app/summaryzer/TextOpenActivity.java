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

import android.provider.OpenableColumns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class TextOpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_open);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.charcoal));
        window.setNavigationBarColor(this.getResources().getColor(R.color.spruce));
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "To be added later", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TextView filehead = findViewById(R.id.textfilename);
        TextView filebody = findViewById(R.id.textfilecontent);

        Intent textintent = getIntent();
        if(textintent.getAction() != null && textintent.getAction().equals(Intent.ACTION_VIEW)){
            String scheme = textintent.getScheme();
            if(scheme != null && scheme.equals(ContentResolver.SCHEME_CONTENT)){
                Uri uri = textintent.getData();
                String filename = getFIleName(uri);
                String filecontent = readFileContent(uri);

                filehead.setText(filename);
                filebody.setText(filecontent);
            }
        }

    }
    private String readFileContent(Uri uri){
        ContentResolver resolver = getContentResolver();
        StringBuilder stringBuilder = new StringBuilder();
        try{
            InputStream inputStream = resolver.openInputStream(uri);
            int i;
            if (inputStream != null) {
                while((i = inputStream.read())!=1){
                    char c = (char) i;
                    stringBuilder.append(c);
                }
            } else{
                Toast.makeText(this,"File Empty", Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e){
            Toast.makeText(this,"File not found", Toast.LENGTH_LONG).show();
        } catch (IOException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return  stringBuilder.toString();
    }
    private String getFIleName(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        Cursor returnCursor = contentResolver.query(uri, null,null,null,null);
        int nameIndex = Objects.requireNonNull(returnCursor).getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String filename = returnCursor.getString(nameIndex);
        returnCursor.close();
        return filename;
    }

}
