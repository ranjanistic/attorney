package com.app.summaryzer.ui.settings;

import android.content.DialogInterface;
import java.lang.*;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.PrecomputedText;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.summaryzer.CustomDialogClass;
import com.app.summaryzer.CustomLoadDialogClass;
import com.app.summaryzer.Login;
import com.app.summaryzer.MainActivity;
import com.app.summaryzer.OnDialogApplyListener;
import com.app.summaryzer.OnDialogConfirmListener;
import com.app.summaryzer.OnDialogLoadListener;
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

        final ImageButton killbutt, remdatabutt, factresetbutt,delaccbutt;
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
                new AuthTask().execute(emailid, pass);
                //authenticate(emailid, pass);

            }
        });

        dialogClassremdata = new CustomDialogClass(getActivity(), new OnDialogConfirmListener() {
            @Override
            public void onApply(Boolean ok) {
              new SettingTask().execute(1);
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
                new SettingTask().execute(2);
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
            public void onApply(Boolean ok)
            {
                new SettingTask().execute(3);
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
                new SettingTask().execute(4);
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
                new SettingTask().execute(5);
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
                dialogbox(R.drawable.ic_settingico,"Sure to log out?","This will log you out from application","Log out","No, abort");
                dialogClasslogout.show();
            }
        });

        return root;
    }


    public class SettingTask extends AsyncTask<Integer,String,String>{
        CustomLoadDialogClass loadAnim;
        @Override
        protected void onPreExecute(){
            loadAnim = new CustomLoadDialogClass(getActivity(), new OnDialogLoadListener() {
                @Override
                public String onLoadText() {
                    return "Please wait";
                }
            });
            loadAnim.show();
        }

        @Override
        protected String doInBackground(Integer... params){
            String text;
            int my  = params[0];
            switch (my){
                case 1: publishProgress("Removing your data");removedata(); text ="Data removed successfully.";  break;
                case 2:publishProgress("Performing reset");factreset(); text="Application reset"; break;
                case 3: publishProgress("Deleting your account");deleteaccount(); text="Account deleted successfully";break;
                case 4: publishProgress("Restarting");killrestart(); text="Restarting..."; break;
                case 5: publishProgress("Logging you out");logout(); text="Logged out successfully"; break;
                default: text="error in code passed";
            }
            return text;
        }
        @Override
        protected void onProgressUpdate(final String... value){
            final String cap = value[0];
            loadAnim = new CustomLoadDialogClass(getActivity(), new OnDialogLoadListener() {
                @Override
                public String onLoadText() {
                    return cap;
                }
            });
            loadAnim.show();
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
            loadAnim = new CustomLoadDialogClass(getActivity(), null);
            loadAnim.dismiss();
        }
    }

    public class AuthTask extends AsyncTask<String,String,Void>{
        CustomLoadDialogClass loadAnim;
        @Override
        protected void onPreExecute(){
            loadAnim = new CustomLoadDialogClass(getActivity(), new OnDialogLoadListener() {
                @Override
                public String onLoadText() {
                    return "Please wait";
                }
            });
            loadAnim.show();
        }

        @Override
        protected Void doInBackground(String... params){
            String mail  = params[0];
            String pass = params[1];
            publishProgress("Authenticating");
            authenticate(mail,pass);
            return null;
        }
        @Override
        protected void onProgressUpdate(final String... value){
            final String cap = value[0];
            loadAnim = new CustomLoadDialogClass(getActivity(), new OnDialogLoadListener() {
                @Override
                public String onLoadText() {
                    return cap;
                }
            });
            loadAnim.show();
        }
        @Override
        protected void onPostExecute(Void v){
            loadAnim = new CustomLoadDialogClass(getActivity(), null);
            Toast.makeText(getActivity(), "onpostexecute", Toast.LENGTH_SHORT).show();
            loadAnim.dismiss();
        }
    }


    private void authenticate(String uid, String passphrase){
        AuthCredential credential = EmailAuthProvider
                .getCredential(uid, passphrase);
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
                            getActivity().finish();
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