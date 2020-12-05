package org.ranjanistic.attorney;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.summaryzer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;

public class ProfileCompletion extends AppCompatActivity {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    EditText name, username, cellnum;
    Button save;
    String TAG = "DBLog", email;
    long phoneNum = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themSetter(getThemeStatus());
        setContentView(R.layout.activity_profile_completion);
        name = findViewById(R.id.userFLname);
        username = findViewById(R.id.useruniquename);
        cellnum = findViewById(R.id.usercell);
        save = findViewById(R.id.saveProfileButt);


        if(user!=null){
            for (UserInfo profile : user.getProviderData()) {
                email = profile.getEmail();
            }
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    phoneNum = NumberFormat.getInstance().parse(cellnum.getText().toString()).longValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(phoneNum != 0) {
                    addUserToDatabase(name.getText().toString(), phoneNum, username.getText().toString(), email);
                } else {
                    Toast.makeText(getApplicationContext(),"Invalid phone number",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addUserToDatabase(String name, long phone,String uid, String mailid){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("worldname", name);
        user.put("username", uid);
        user.put("cellnumeral", phone);
// Add a new document with a generated ID
        db.collection("associatedtextdata").document(mailid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_LONG).show();
                    }
                });
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
