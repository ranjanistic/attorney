package com.app.summaryzer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageCofirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_image_cofirmation);
        String imagepath = getImagePath();

        ImageView imageView = findViewById(R.id.captured_img_preview);
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagepath));

        ImageButton redobtn = findViewById(R.id.retakeButt);
        redobtn.setOnClickListener(view -> finish());

        ImageButton cancelbtn = findViewById(R.id.cancelCaptureButt);
        cancelbtn.setOnClickListener(view -> {
            Intent i=new Intent(ImageCofirmation.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
            startActivity(i);
        });

        ImageButton donebtn = findViewById(R.id.doneCaptureButt);
        donebtn.setOnClickListener(view -> {
            Intent i=new Intent(ImageCofirmation.this, TextOpenActivity.class);
            startActivity(i);
        });
    }

    private void themSetter(int tcode){
        switch (tcode){
            case 101: this.setTheme(R.style.AppTheme);break;
            case 102: this.setTheme(R.style.LightTheme);break;
            case 103: this.setTheme(R.style.joyTheme);break;
        }
    }
    private int getThemeStatus(){
        SharedPreferences mSharedPreferences = this.getSharedPreferences("theme", MODE_PRIVATE);
        return mSharedPreferences.getInt("themeCode", 0);
    }

    private String getImagePath(){
        SharedPreferences mSharedPreferences = this.getSharedPreferences("ImageCaptured", MODE_PRIVATE);
        return mSharedPreferences.getString("imagepath", "");
    }
}
