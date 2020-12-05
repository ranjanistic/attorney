package org.ranjanistic.attorney

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.ranjanistic.attorney.dialog.CustomLoadDialogClass
import org.ranjanistic.attorney.listener.OnDialogLoadListener
import java.util.*

class ConfirmationActivity : AppCompatActivity() {
    var caption: String? = null
    var loginpath: String? = null
    var continueMessage: String? = null
    var imageCode: String? = null
    var cCaption: TextView? = null
    var cLoginPath: TextView? = null
    var cMessage: TextView? = null
    var cBackImg: ImageView? = null
    private var firebaseAuth: FirebaseAuth? = null
    var loadingwhilelogin: CustomLoadDialogClass? = null
    var gso: GoogleSignInOptions? = null
    private var googleApiClient: GoogleApiClient? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    var idToken: String? = null
    var pname: String? = null
    var pEmail: String? = null
    var pPhoto: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        caption = Objects.requireNonNull(bundle).getString("contTitle")
        loginpath = Objects.requireNonNull(bundle).getString("contVia")
        continueMessage = Objects.requireNonNull(bundle).getString("contText")
        imageCode = Objects.requireNonNull(bundle).getString("contCode")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        val activityFull = findViewById<View>(R.id.confirmationActivity)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        firebaseAuth = FirebaseAuth.getInstance()
        cCaption = findViewById(R.id.continueTitle)
        cLoginPath = findViewById(R.id.continueAs)
        cMessage = findViewById(R.id.continueInfo)
        cBackImg = findViewById(R.id.continueBackImage)
        cCaption?.text = caption
        cLoginPath?.text = loginpath
        cMessage?.text = continueMessage
        if (imageCode == "g") {
            window.statusBarColor = this.resources.getColor(R.color.white)
            window.navigationBarColor = this.resources.getColor(R.color.white)
            cMessage?.setTypeface(Typeface.DEFAULT_BOLD)
            cCaption?.setTextColor(resources.getColor(R.color.black))
            cLoginPath?.setTextColor(resources.getColor(R.color.black))
            cMessage?.setTextColor(resources.getColor(R.color.black))
            cBackImg?.setImageDrawable(resources.getDrawable(R.drawable.ic_googleglogo))
            activityFull.setBackgroundColor(resources.getColor(R.color.white))
        } else if (imageCode == "a") {
            window.statusBarColor = this.resources.getColor(R.color.black)
            window.navigationBarColor = this.resources.getColor(R.color.black)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            cCaption?.setTextColor(resources.getColor(R.color.white))
            cLoginPath?.setTextColor(resources.getColor(R.color.white))
            cMessage?.setTextColor(resources.getColor(R.color.white))
            cBackImg?.setImageDrawable(resources.getDrawable(R.drawable.ic_icognitoman))
            activityFull.setBackgroundColor(resources.getColor(R.color.black))
            gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id)) //you can also use R.string.default_web_client_id
                    .requestEmail()
                    .build()
            googleApiClient = GoogleApiClient.Builder(this)
                    .enableAutoManage(this) { connectionResult -> t(connectionResult.toString()) }
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso!!)
                    .build()
        }
        loadingwhilelogin = CustomLoadDialogClass(this, object : OnDialogLoadListener {
            override fun onLoad() {}
            override fun onLoadText(): String {
                return "Working"
            }
        })
        val abortlogin = findViewById<Button>(R.id.cancelLogin)
        abortlogin.setOnClickListener { finish() }
        val contlogin = findViewById<Button>(R.id.continueLogin)
        contlogin.setOnClickListener {
            loadingwhilelogin!!.show()
            if (imageCode == "a") {
                anonymLogin()
            } else if (imageCode == "g") {
                val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            t("three")
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleGoogleLogResult(result)
        } else if (requestCode == AN_SIGN_IN) {
            anonymLogin()
        }
    }

    private fun anonymLogin() {
        t("no symbiosis")
    }

    private fun handleGoogleLogResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            t("four")
            val account = result.signInAccount
            idToken = account!!.idToken
            pname = account.displayName
            pEmail = account.email
            pPhoto = account.photoUrl
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            t("five")
            firebaseAuthWGoogle(credential)
        } else {
            t("failedfive")
        }
        loadingwhilelogin!!.dismiss()
    }

    private fun firebaseAuthWGoogle(cred: AuthCredential) {
        firebaseAuth!!.signInWithCredential(cred)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        t("Symbiosis")
                        backToProfile()
                    } else {
                        task.exception!!.printStackTrace()
                        t("Can't auth")
                    }
                }
    }

    private fun backToProfile() {
        val i = Intent(this@ConfirmationActivity, AccountView::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        i.putExtra("name", pname)
        i.putExtra("mailid", pEmail)
        i.putExtra("photo", pPhoto.toString())
        startActivity(i)
    }

    private fun t(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    companion object {
        var RC_SIGN_IN = 1
        var AN_SIGN_IN = 2
    }
}