package org.ranjanistic.attorney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        themSetter(getThemeStatus());
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void themSetter(int tcode){
        switch (tcode){
            case 101: setTheme(R.style.splashTheme);
                break;
            case 102: setTheme(R.style.splashThemeLight);
                break;
            case 103: setTheme(R.style.splashThemeJoy);
                break;
        }
    }
    private int getThemeStatus() {
        SharedPreferences mSharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        return mSharedPreferences.getInt("themeCode", 0);
    }

}
