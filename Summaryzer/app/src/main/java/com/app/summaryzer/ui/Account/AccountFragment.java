package com.app.summaryzer.ui.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.summaryzer.CustomLoadDialogClass;
import com.app.summaryzer.Login;
import com.app.summaryzer.OnDialogLoadListener;
import com.app.summaryzer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.net.URI;
import java.net.URL;
import java.util.Objects;

public class AccountFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseUser user;
    CustomLoadDialogClass loadDialogWhileLinkGen;
    private AccountViewModel accountViewModel;
    private Boolean signed = false;
    private Button signin;
    ScrollView scrollView;
    private TextView mailtext, logoutstatus, verifytxt;
    ImageButton verifybutt;
    ImageView accountico;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        signin = root.findViewById(R.id.signinbuttabout);
        mailtext = root.findViewById(R.id.accountdisptext);
        logoutstatus = root.findViewById(R.id.loggedouttext);
        scrollView = root.findViewById(R.id.loginscrollview);
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        verifybutt = root.findViewById(R.id.mailverifybtn);
        verifytxt = root.findViewById(R.id.verifystatustxt);



        if (user != null) {
            signedin(user);
        } else {
            signedout();
        }
        return root;
    }

    private void signedin(FirebaseUser firebaseUser){
        String name = "", email = "";
        String providerId = "", uid="";
        Uri photoUrl;
        if (firebaseUser!=null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                providerId = profile.getProviderId();

                // UID specific to the provider
                uid = profile.getUid();

                // Name, email address, and profile photo Url
                name = profile.getDisplayName();
                email = profile.getEmail();
                photoUrl = profile.getPhotoUrl();
            }
        }
        logoutstatus.setVisibility(View.GONE);
        signin.setVisibility(View.GONE);
        final String text = email;

        loadDialogWhileLinkGen = new CustomLoadDialogClass(getContext(), new OnDialogLoadListener() {
            @Override
            public void onLoad() {

            }

            @Override
            public String onLoadText() {
                return "Sending link at "+ text;
            }
        });

        mailtext.setText(text);
        mailtext.setTextColor(getResources().getColor(R.color.green));
        checkIfEmailVerified();
        verifybutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialogWhileLinkGen.show();
                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(),"Link has been sent at "+text,Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(),"error",Toast.LENGTH_LONG).show();
                                }
                                loadDialogWhileLinkGen.dismiss();
                            }
                        });
            }
        });
    }

    private void signedout(){
        scrollView.setVisibility(View.INVISIBLE);
        logoutstatus.setVisibility(View.VISIBLE);
        signin.setVisibility(View.VISIBLE);
        logoutstatus.setText(R.string.loggedas);
        logoutstatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent logintent = new Intent(getActivity(), Login.class);
                startActivity(logintent);
            }
        });
    }
    private void checkIfEmailVerified()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (Objects.requireNonNull(user).isEmailVerified())
        {
            verifytxt.setText(R.string.verified_text);
            verifytxt.setTextColor(getResources().getColor(R.color.green));
            verifytxt.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else
        {
            verifytxt.setText(R.string.unverified_verify_now);
            verifytxt.setTextColor(getResources().getColor(R.color.dark_red));
            verifytxt.setTypeface(Typeface.DEFAULT);

        }
    }

}