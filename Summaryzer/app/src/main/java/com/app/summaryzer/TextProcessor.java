package com.app.summaryzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

public class TextProcessor extends AppCompatActivity {
    ViewPager slideViewPager;
    LinearLayout dotLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_text_processor);
        slideViewPager = findViewById(R.id.slideViewer);
        dotLayoutView = findViewById(R.id.dotLayout);

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
