package org.ranjanistic.attorney;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.app.summaryzer.R;

import org.ranjanistic.attorney.dialog.CustomLoadDialogClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.annotations.NonNull;

public class TextOpenActivity extends AppCompatActivity {
    String fpath;
    String head, body,location ;
    NestedScrollView contentScrollView;
    CustomLoadDialogClass whilefilereadload;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
            if(user!=null){
                String email = "";
                if(checknet()) {
                    for (UserInfo profile : user.getProviderData()) {
                        email = profile.getEmail();
                    }
                    addContentToDatabase(body, email);
                }
            }
        }

        if(bundle==null){
            String[] recentClickData = getRecentDataToOpen();
            filehead.setText(recentClickData[0]);
            filebody.setText(recentClickData[1]);
            filepath.setVisibility(View.INVISIBLE);
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
    boolean checknet() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }

    private void addContentToDatabase(String content, String mailid){
        Map<String, Object> data = new HashMap<>();
        data.put("texture", content);
        db.collection("associatedtextdata").document(mailid).collection("texture")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"failDone",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String[] getRecentDataToOpen(){
        SharedPreferences mSharedPreferences = getSharedPreferences("recentviewdata", MODE_PRIVATE);
        String[] rdata = {mSharedPreferences.getString("heading", ""), mSharedPreferences.getString("body", "")};
        return rdata;
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