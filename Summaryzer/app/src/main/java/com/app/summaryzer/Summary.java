package com.app.summaryzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class Summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_summary);
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
