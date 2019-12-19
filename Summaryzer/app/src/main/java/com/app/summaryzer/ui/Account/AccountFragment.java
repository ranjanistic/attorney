package com.app.summaryzer.ui.Account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.summaryzer.Login;
import com.app.summaryzer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class AccountFragment extends Fragment {

    private FirebaseUser user;
    private AccountViewModel accountViewModel;
    private Boolean signed = false;
    private Button signin;
    private TextView disptext;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        signin = root.findViewById(R.id.signinbuttabout);
        disptext = root.findViewById(R.id.accountdisptext);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            signedin();
        } else {
            disptext.setText(R.string.loggedas);
            disptext.setTextColor(getResources().getColor(R.color.colorPrimary));
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
        return root;
    }

    private void signedin(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String name = "", email = "", text;
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
        signin.setVisibility(View.INVISIBLE);
        text = "Logged in as "+ email;
        Toast.makeText(getActivity(),"lmao"+providerId,Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(),uid,Toast.LENGTH_SHORT).show();
        disptext.setText(text);
        disptext.setTextColor(getResources().getColor(R.color.white));
    }
}