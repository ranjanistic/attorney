package org.ranjanistic.attorney.ui.settings;

/*
    Settings fragment for AccountVew activity.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.ranjanistic.attorney.Login;
import org.ranjanistic.attorney.MainActivity;
import org.ranjanistic.attorney.R;
import org.ranjanistic.attorney.dialog.CustomAlertDialog;
import org.ranjanistic.attorney.dialog.CustomConfirmDialogClass;
import org.ranjanistic.attorney.dialog.CustomLoadDialogClass;
import org.ranjanistic.attorney.dialog.CustomOnOptListener;
import org.ranjanistic.attorney.dialog.CustomTextDialog;
import org.ranjanistic.attorney.dialog.CustomVerificationDialog;
import org.ranjanistic.attorney.listener.OnDialogAlertListener;
import org.ranjanistic.attorney.listener.OnDialogApplyListener;
import org.ranjanistic.attorney.listener.OnDialogConfirmListener;
import org.ranjanistic.attorney.listener.OnDialogLoadListener;
import org.ranjanistic.attorney.listener.OnDialogTextListener;
import org.ranjanistic.attorney.listener.OnOptionChosenListener;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private SettingsViewModel settingsViewModel;
    private CustomConfirmDialogClass dialogClassremdata,dialogClassfactreset,dialogClassdelacc,dialogClasskillres, dialogClasslogout, restartConfirmationDialog;
    private CustomAlertDialog alertDialogBox, netErrorDialog;
    private CustomOnOptListener themechoserdialog;
    private CustomLoadDialogClass loadAnim, loadDialogClassWhileReset;
    private CustomTextDialog passwordResetDialog;
    private ImageView settingIcon;
    private String emailid, pass, resetEmail,textDhead, textDsubhead, textsubmit, textCancel, textDhint;
    private  String heading, subheading, posbtntxt, negbtntxt, alheading, alsubheading;
    private Drawable dialogimage, alertImage, textDimg;
    //actcode 1(remove data), 2(fact reset), 3(delete account), 4(kill and restart)

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final ImageButton killbutt, remdatabutt, factresetbutt,delaccbutt, passwordResetButt, themeChangeButt ;
        Button logoutbutt;
        settingIcon = root.findViewById(R.id.appiconsetting);
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.cycle_recycle);
        rotation.setFillAfter(true);
        settingIcon.startAnimation(rotation);
        killbutt = root.findViewById(R.id.killrestartbtn);
        remdatabutt = root.findViewById(R.id.removedatabtn);
        factresetbutt = root.findViewById(R.id.resetbtn);
        delaccbutt = root.findViewById(R.id.accdeletebtn);
        passwordResetButt = root.findViewById(R.id.passchangebtn);
        themeChangeButt = root.findViewById(R.id.themechangebtn);
        logoutbutt = root.findViewById(R.id.signoutbtn);

        /* Verification Dialog before account deletion */
        final CustomVerificationDialog dialog = new CustomVerificationDialog(getActivity(), new OnDialogApplyListener() {
            @Override
            public void onApply(String demail, String dpassword) {
                emailid = demail;
                pass = dpassword;
                new AuthTask().execute(emailid, pass);

            }
        });

        /*Dialog box class object for failed deletion */

        alertDialogBox = new CustomAlertDialog(getActivity(), new OnDialogAlertListener() {
            @Override
            public String onCallText() {
                return alheading;
            }

            @Override
            public String onCallSub() {
                return alsubheading;
            }

            @Override
            public Drawable onCallImg() {
                return alertImage;
            }
        });

        //dialog box for network failure
        netErrorDialog = new CustomAlertDialog(getActivity(), new OnDialogAlertListener() {
            @Override
            public String onCallText() {
                return alheading;
            }

            @Override
            public String onCallSub() {
                return alsubheading;
            }

            @Override
            public Drawable onCallImg() {
                return alertImage;
            }
        });

        //dialog class object for data removal
        dialogClassremdata = new CustomConfirmDialogClass(getActivity(), new OnDialogConfirmListener() {
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

        //dialog class object for factory reset
        dialogClassfactreset = new CustomConfirmDialogClass(getActivity(), new OnDialogConfirmListener() {
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

        //dialog class object for account deletion

        dialogClassdelacc = new CustomConfirmDialogClass(getActivity(), new OnDialogConfirmListener() {
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

        //dialog class object for restart
        dialogClasskillres = new CustomConfirmDialogClass(getActivity(), new OnDialogConfirmListener() {
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

        loadDialogClassWhileReset = new CustomLoadDialogClass(getContext(), new OnDialogLoadListener() {
            @Override
            public void onLoad() {
                new SettingTask().execute(5);
            }

            @Override
            public String onLoadText() {
                return "Sending Link";
            }
        });


        //dialog class object for logging out
        dialogClasslogout = new CustomConfirmDialogClass(getActivity(), new OnDialogConfirmListener() {
            @Override
            public void onApply(Boolean ok) {
                new SettingTask().execute(6);
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
        });

        passwordResetDialog = new CustomTextDialog(getContext(), new OnDialogTextListener() {
            @Override
            public void onApply(String text) {
                resetEmail = text;
                loadDialogClassWhileReset.show();
            }

            @Override
            public String onCallText() {
                return textDhead;
            }

            @Override
            public String onCallSubText() {
                return textDsubhead;
            }

            @Override
            public String onCallPos() {
                return textsubmit;
            }

            @Override
            public String onCallNeg() {
                return textCancel;
            }

            @Override
            public String onCallHint(){
                return textDhint;
            }

            @Override
            public Drawable onCallImg() {
                return textDimg;
            }
        });

        restartConfirmationDialog = new CustomConfirmDialogClass(getContext(), new OnDialogConfirmListener() {
            @Override
            public void onApply(Boolean confirm) {
                Toast.makeText(getContext(),"Theme applied",Toast.LENGTH_LONG).show();
                killrestart();
            }

            @Override
            public String onCallText() {
                return "Restart to apply changes";
            }

            @Override
            public String onCallSub() {
                return "New theme will be fully functional after a restart.";
            }

            @Override
            public String onCallPos() {
                return "Restart now";
            }

            @Override
            public String onCallNeg() {
                return "Later";
            }

            @Override
            public Drawable onCallImg() {
                return getResources().getDrawable(R.drawable.ic_trashico);
            }
        });

        themechoserdialog = new CustomOnOptListener(getContext(), new OnOptionChosenListener() {
            @Override
            public void onChoice(int choice) {
                storeThemeStatus(choice);
                themSetter(choice);
                restartConfirmationDialog.setCanceledOnTouchOutside(false);
                restartConfirmationDialog.show();
            }

            @Override
            public String onCallHeader() {
                return "Choose a theme";
            }

            @Override
            public Drawable onCallImage() {
                return getResources().getDrawable(R.drawable.ic_looksico);
            }
        });
        //restart button listener
        killbutt.setOnClickListener(new View.OnClickListener(){
           @Override
                   public void onClick(View v){
               dialogbox(R.drawable.ic_resetting,"Force stop the app and restart?","Restarting the app can boost it sometimes.","Restart","Cancel");
               dialogClasskillres.show();
           }
        });

        //data remove button listener
        remdatabutt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checknet()){
                dialogbox(R.drawable.ic_trashico,"Delete all existing data?","This includes deleting everything from our servers and your device related to you and the app.\nIrreversible, proceed with caution.","Delete my data","Abort");
                dialogClassremdata.show();}
                else {
                    alertBox(R.drawable.ic_disconnectiontower,"Network Failure","Error reaching server. Check your internet connection.");
                    netErrorDialog.show();
                }

            }
        });

        //factory reset button listener
        factresetbutt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogbox(R.drawable.ic_resetting,"Reset all app data?","This will bring the app to its initial state","Yes, reset","Cancel");
                dialogClassfactreset.show();
            }
        });

        //account delete button listener
        delaccbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {       //if not logged in
                    Toast.makeText(getContext(), "You need to sign in first.", Toast.LENGTH_LONG).show();
                    Intent logint = new Intent(getActivity(), Login.class);
                    startActivity(logint);
                    getActivity().finish();
                } else {
                    dialog.show();      //before deleting verification dialog
                }
            }
        });

        //pass word change button
        passwordResetButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null){
                    Toast.makeText(getContext(), "You need to sign in first.", Toast.LENGTH_LONG).show();
                    Intent logint = new Intent(getActivity(), Login.class);
                    startActivity(logint);
                }else {
                    if (checknet()) {
                        editTextBox(R.drawable.ic_changepassword, "Password reset", "Enter your email ID to receive a temporary password reset link..", "Send Link", "Abort", "Type email ID");
                        passwordResetDialog.show();
                    } else {
                        alertBox(R.drawable.ic_disconnectiontower, "Network Failure", "Error reaching server. Check your internet connection.");
                        netErrorDialog.show();
                    }
                }
            }
        });

        themeChangeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themechoserdialog.show();
            }
        });

        //logout button listener
        logoutbutt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checknet()) {
                    dialogbox(R.drawable.ic_settingico, "Sure to log out?", "This will log you out from application", "Log out", "No, abort");
                    dialogClasslogout.show();
                } else {
                    alertBox(R.drawable.ic_disconnectiontower,"Network Failure","Error reaching server. Check your internet connection.");
                    netErrorDialog.show();
                }
            }
        });

        return root;
    }


    //For each setting option asynctask
    public class SettingTask extends AsyncTask<Integer,String,String>{
        @Override
        protected String doInBackground(Integer... params){
            String text;
            int execCode  = params[0];
            switch (execCode){
                /* The following execution codes are defined as per the order of appearance in activity layout.
                      Order can be changed by changing codes and respective functions accordingly.
                * 1 for data removal
                * 2 for factory reset
                * 3 for account deletion
                * 4 for restarting
                * 5 for logout
                * 101 dark theme
                * 102 light theme
                * 103 joytheme
                 */
                case 1: publishProgress("Removing your data");removedata(); text ="Data removed successfully.";  break;
                case 2:publishProgress("Performing reset");factreset(); text="Application reset successfully"; break;
                case 3: publishProgress("Deleting your account");deleteaccount(); text="Account deleted successfully";break;
                case 4: publishProgress("Restarting");killrestart(); text="Restarted."; break;
                case 5: changePass(); text="Link generated."; break;
                case 6: publishProgress("Logging you out");logout(); text="Logged out successfully"; break;
                default: text="error in code passed";
            }
            return text;
        }
        @Override
        protected void onProgressUpdate(final String... value){
            final String cap = value[0];
            loadAnim = new CustomLoadDialogClass(getActivity(), new OnDialogLoadListener() {
                @Override
                public void onLoad(){
                    Toast.makeText(getContext(),"Working on it", Toast.LENGTH_SHORT).show();
                }
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
            loadAnim.dismiss();     //TODO: Loading animation not dismissing even after completion of asyncTask.
        }
    }

    //For authentication asynctask
    public class AuthTask extends AsyncTask<String,String,Integer>{
        @Override
        protected Integer doInBackground(String... params){
            String mail  = params[0];
            String pass = params[1];

            publishProgress("Authenticating");
            if(checknet()){
                authenticate(mail,pass);
                return 1;
            }
         else {
                alertBox(R.drawable.ic_disconnectiontower,"Network Failure","Error reaching server. Check your internet connection.");
                return 0;
            }

        }
        @Override
        protected void onProgressUpdate(final String... value){
            final String cap = value[0];
            loadAnim = new CustomLoadDialogClass(getActivity(), new OnDialogLoadListener() {
                @Override
                public void onLoad(){
                    Toast.makeText(getContext(),"Working on it", Toast.LENGTH_SHORT).show();
                }
                @Override
                public String onLoadText() {
                    return cap;
                }
            });
            loadAnim.show();
        }
        @Override
        protected void onPostExecute(Integer i){
        if(i==0){       //not connected
            netErrorDialog.show();
            loadAnim.dismiss();
        }
            loadAnim = new CustomLoadDialogClass(getActivity(), null);
            if(loadAnim.isShowing()){
                loadAnim.dismiss();
            }
        }
    }

    //Authenticator function before any permanent account action.
    private void authenticate(String uid, String passphrase){
            AuthCredential credential = EmailAuthProvider
                    .getCredential(uid, passphrase);
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Authentication passed", Toast.LENGTH_SHORT).show();
                                dialogbox(R.drawable.ic_accountremove, "Confirm to delete your accout?", "This act is permanent, and your account will be unrecoverable.", "Yes, delete my account", "No, abort");
                                dialogClassdelacc.show();
                            } else {
                                alertBox(R.drawable.ic_warning, "Failed to verify", "Credentials you entered were wrong.");
                                alertDialogBox.show();
                            }
                        }
                    });
    }


    //Factory reset function
    private void factreset() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear org.ranjanistic.attorney");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Account deletion function (to be called AFTER re-authentication).
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

    //Log out function
    private void logout(){
//            Intent i=new Intent(getActivity(), MainActivity.class);
     //       i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
          //  startActivity(i);
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
    }

    //Restart function
    private void killrestart(){
        Intent i=new Intent(getActivity(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        startActivity(i);
    }

    //Data removal function
    private void removedata(){
        Toast.makeText(getActivity(), "data removal", Toast.LENGTH_SHORT).show();
    }

    private void changePass(){
        if (checknet()) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(resetEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Success", "Email sent.");
                                Toast.makeText(getContext(), "Link sent!", Toast.LENGTH_LONG).show();
                                loadDialogClassWhileReset.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Error link not sent", Toast.LENGTH_LONG).show();
                                loadDialogClassWhileReset.dismiss();
                            }
                        }
                    });
        }
    }

    private void themSetter(int tcode){
        switch (tcode){
            case 101: this.getActivity().setTheme(R.style.AppTheme);break;
            case 102: this.getActivity().setTheme(R.style.LightTheme);break;
            case 103: this.getActivity().setTheme(R.style.joyTheme);break;
            default:this.getActivity().setTheme(R.style.AppTheme);
        }
    }
    private int getThemeStatus(){
        SharedPreferences mSharedPreferences = this.getActivity().getSharedPreferences("theme", MODE_PRIVATE);
        return mSharedPreferences.getInt("themeCode", 0);
    }

    private void storeThemeStatus(int themechoice){
        SharedPreferences mSharedPreferences = this.getActivity().getSharedPreferences("theme", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("themeCode", themechoice);
        mEditor.apply();
    }




    //dialog box content loader
    private void dialogbox(int dimg,String h, String sh, String pbt, String nbt){
        dialogimage = getResources().getDrawable(dimg);
        heading = h;
        subheading = sh;
        posbtntxt  = pbt;
        negbtntxt = nbt;
    }

    //alert box content loader
    private void alertBox(int aimg, String h, String sh){
        alertImage = getResources().getDrawable(aimg);
        alheading = h;
        alsubheading = sh;
    }


    private void editTextBox(int timg, String h, String sh, String tsubmit, String tcancel, String thint){
        textDimg = getResources().getDrawable(timg);
        textDhead = h;
        textDsubhead = sh;
        textsubmit = tsubmit;
        textCancel = tcancel;
        textDhint = thint;
    }
    

    //network checker function
    boolean checknet() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }

}