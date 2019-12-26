package com.app.summaryzer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.app.summaryzer.ui.Account.AccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Objects;

public class AccountView extends AppCompatActivity{
    String pname,pEmail,pPhoto;
    Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            pname = Objects.requireNonNull(bundle).getString("name");
            pEmail = Objects.requireNonNull(bundle).getString("mailid");
            pPhoto = Objects.requireNonNull(bundle).getString("photo");
            Bundle newbundle = new Bundle();
            newbundle.putString("name", pname);
            newbundle.putString("mailid", pEmail);
            newbundle.putString("photo", pPhoto);
            AccountFragment Accfragobj = new AccountFragment();
            Accfragobj.setArguments(newbundle);
        }
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_account_view);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_account, R.id.navigation_settings, R.id.navigation_about)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    //    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
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
