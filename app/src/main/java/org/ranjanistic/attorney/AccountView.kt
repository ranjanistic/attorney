package org.ranjanistic.attorney

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.ranjanistic.attorney.ui.Account.AccountFragment
import java.util.*

class AccountView : AppCompatActivity() {
    var pname: String? = null
    var pEmail: String? = null
    var pPhoto: String? = null
    //var window: Window? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null) {
            pname = Objects.requireNonNull(bundle).getString("name")
            pEmail = Objects.requireNonNull(bundle).getString("mailid")
            pPhoto = Objects.requireNonNull(bundle).getString("photo")
            val newbundle = Bundle()
            newbundle.putString("name", pname)
            newbundle.putString("mailid", pEmail)
            newbundle.putString("photo", pPhoto)
            val Accfragobj = AccountFragment()
            Accfragobj.arguments = newbundle
        }
        themSetter(themeStatus)
        setContentView(R.layout.activity_account_view)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_account, R.id.navigation_settings, R.id.navigation_about)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        //    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController)
    }

    private fun themSetter(tcode: Int) {
        when (tcode) {
            101 -> setTheme(R.style.AppTheme)
            102 -> setTheme(R.style.LightTheme)
            103 -> setTheme(R.style.joyTheme)
        }
    }

    private val themeStatus: Int
        private get() {
            val mSharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            return mSharedPreferences.getInt("themeCode", 0)
        }
}