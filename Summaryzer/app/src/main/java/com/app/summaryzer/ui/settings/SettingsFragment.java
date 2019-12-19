package com.app.summaryzer.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.summaryzer.CustomDialogClass;
import com.app.summaryzer.Login;
import com.app.summaryzer.MainActivity;
import com.app.summaryzer.OnDialogApplyListener;
import com.app.summaryzer.OnDialogConfirmListener;
import com.app.summaryzer.R;
import com.app.summaryzer.VerificationDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class SettingsFragment extends Fragment {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private SettingsViewModel settingsViewModel;
    private CustomDialogClass dialogClassremdata,dialogClassfactreset,dialogClassdelacc,dialogClasskillres, dialogClasslogout;
    private String emailid, pass;
    private String heading, subheading, posbtntxt, negbtntxt;
    private Drawable dialogimage;
    private int posbtncol, negbtncol, actioncode ; //actcode 1(remove data), 2(fact reset), 3(delete account), 4(kill and restart)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);

        ImageButton killbutt, remdatabutt, factresetbutt,delaccbutt;
        Button logoutbutt;
        killbutt = root.findViewById(R.id.killrestartbtn);
        remdatabutt = root.findViewById(R.id.removedatabtn);
        factresetbutt = root.findViewById(R.id.resetbtn);
        delaccbutt = root.findViewById(R.id.accdeletebtn);
        logoutbutt = root.findViewById(R.id.signoutbtn);

        final VerificationDialog dialog = new VerificationDialog(getActivity(), new OnDialogApplyListener() {
            @Override
            public void onApply(String demail, String dpassword) {
                Toast.makeText(getContext(), "Assigning values.", Toast.LENGTH_LONG).show();
                emailid = demail;
                pass = dpassword;
                authenticate(emailid, pass);

            }
        });

        dialogClassremdata = new CustomDialogClass(getActivity(), new OnDialogConfirmListener() {
            @Override
            public void onApply(Boolean ok) {
                removedata();
            }

            @Override
            public String onCallText() {
                return heading;
            }

            @Override
            public String onCallSub() {
                return subheading;
            }

            @Override
            public String onCallPos() {
                return posbtntxt;
            }

            @Override
            public String onCallNeg() {
                return negbtntxt;
            }
            @Override
            public Drawable onCallImg(){
                return dialogimage;
            }
            /*@Override  TODO:Button Color setting and dialogbox function change accordingly
            public int onCallPoscol(){
                return posbtncol;
            }
            @Override
            public int onCallNegcol(){
                return negbtncol;
            }*/
        });

        dialogClassfactreset = new CustomDialogClass(getActivity(), new OnDialogConfirmListener() {
            @Override
            public void onApply(Boolean ok) {
                factreset();
            }

            @Override
            public String onCallText() {
                return heading;
            }

            @Override
            public String onCallSub() {
                return subheading;
            }

            @Override
            public String onCallPos() {
                return posbtntxt;
            }

            @Override
            public String onCallNeg() {
                return negbtntxt;
            }
            @Override
            public Drawable onCallImg(){
                return dialogimage;
            }
            /*@Override  TODO:Button Color setting and dialogbox function change accordingly
            public int onCallPoscol(){
                return posbtncol;
            }
            @Override
            public int onCallNegcol(){
                return negbtncol;
            }*/
        });

        dialogClassdelacc = new CustomDialogClass(getActivity(), new OnDialogConfirmListener() {
            @Override
            public void onApply(Boolean ok) {
                deleteaccount();
            }

            @Override
            public String onCallText() {
                return heading;
            }

            @Override
            public String onCallSub() {
                return subheading;
            }

            @Override
            public String onCallPos() {
                return posbtntxt;
            }

            @Override
            public String onCallNeg() {
                return negbtntxt;
            }
            @Override
            public Drawable onCallImg(){
                return dialogimage;
            }
            /*@Override  TODO:Button Color setting and dialogbox function change accordingly
            public int onCallPoscol(){
                return posbtncol;
            }
            @Override
            public int onCallNegcol(){
                return negbtncol;
            }*/
        });
        dialogClasskillres = new CustomDialogClass(getActivity(), new OnDialogConfirmListener() {
            @Override
            public void onApply(Boolean ok) {
                killrestart();
            }

            @Override
            public String onCallText() {
                return heading;
            }

            @Override
            public String onCallSub() {
                return subheading;
            }

            @Override
            public String onCallPos() {
                return posbtntxt;
            }

            @Override
            public String onCallNeg() {
                return negbtntxt;
            }
            @Override
            public Drawable onCallImg(){
                return dialogimage;
            }
            /*@Override  TODO:Button Color setting and dialogbox function change accordingly
            public int onCallPoscol(){
                return posbtncol;
            }
            @Override
            public int onCallNegcol(){
                return negbtncol;
            }*/
        });
        dialogClasslogout = new CustomDialogClass(getActivity(), new OnDialogConfirmListener() {
            @Override
            public void onApply(Boolean ok) {
                logout();
            }

            @Override
            public String onCallText() {
                return heading;
            }

            @Override
            public String onCallSub() {
                return subheading;
            }

            @Override
            public String onCallPos() {
                return posbtntxt;
            }

            @Override
            public String onCallNeg() {
                return negbtntxt;
            }
            @Override
            public Drawable onCallImg(){
                return dialogimage;
            }
            /*@Override  TODO:Button Color setting and dialogbox function change accordingly
            public int onCallPoscol(){
                return posbtncol;
            }
            @Override
            public int onCallNegcol(){
                return negbtncol;
            }*/
        });


        killbutt.setOnClickListener(new View.OnClickListener(){
           @Override
                   public void onClick(View v){
               dialogbox(R.drawable.ic_resetting,"Force stop the app and restart?","Restarting the app can boost it sometimes.","Restart","Cancel");
               dialogClasskillres.show();
           }
        });
        remdatabutt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogbox(R.drawable.ic_trashico,"Delete all existing data?","This includes deleting everything from our servers and your device related to you and the app.\nIrreversible, proceed with caution.","Delete my data","Abort");
                dialogClassremdata.show();
            }
        });
        factresetbutt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogbox(R.drawable.ic_resetting,"Reset all app data?","This will bring the app to its initial state","Yes, reset","Cancel");
                dialogClassfactreset.show();
            }
        });


        delaccbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {       //if not logged in
                    Toast.makeText(getContext(), "You need to sign in first.", Toast.LENGTH_LONG).show();
                    Intent logint = new Intent(getActivity(), Login.class);
                    startActivity(logint);
                } else {
                    dialog.show();      //before deleting verification dialog
                }
            }
        });
        logoutbutt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                actioncode = 5;
                dialogbox(R.drawable.ic_settingico,"Sure to log out?","This will log you out from application","Log out","No, abort");
                dialogClasslogout.show();
                actioncode = 0;
            }
        });

        return root;
    }

    private void authenticate(String uid, String passphrase){
        AuthCredential credential = EmailAuthProvider
                .getCredential(uid, passphrase);
        Toast.makeText(getContext(), "Credential Passed.", Toast.LENGTH_LONG).show();
        user.reauthenticate(credential)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Authentication passed", Toast.LENGTH_SHORT).show();
                            dialogbox(R.drawable.ic_accountremove,"Confirm to delete your accout?","This act is permanent, and your account will be unrecoverable.","Yes, delete my account","No, abort");
                            dialogClassdelacc.show();
                        } else {
                            Toast.makeText(getActivity(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //private void chooseAction(int actcode){
        //switch (actcode){

//            default: actcode = 0;
//        }
    //}
    private void factreset() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear com.app.summaryzer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteaccount(){
        user.delete()
                .addOnCompleteListener (new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void logout(){
            Intent i=new Intent(getActivity(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
            startActivity(i);
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(), "logged out successfully", Toast.LENGTH_SHORT).show();
    }
    private void killrestart(){
        Intent i=new Intent(getActivity(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        startActivity(i);
    }

    private void removedata(){
        Toast.makeText(getActivity(), "data removal", Toast.LENGTH_SHORT).show();
    }

    private void dialogbox(int dimg,String h, String sh, String pbt, String nbt){
        dialogimage = getResources().getDrawable(dimg);
        heading = h;
        subheading = sh;
        posbtntxt  = pbt;
        negbtntxt = nbt;
    }
    //private String Heading(String headtxt){
        //return headtxt;
    //}
}