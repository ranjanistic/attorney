package com.app.summaryzer.ui.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.app.summaryzer.AccountView;
import com.app.summaryzer.CustomLoadDialogClass;
import com.app.summaryzer.Login;
import com.app.summaryzer.OnDialogLoadListener;
import com.app.summaryzer.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class AccountFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private GoogleSignInAccount Gaccount;
    private CustomLoadDialogClass loadDialogWhileLinkGen;
    private AccountViewModel accountViewModel;
    private Boolean signed = false;
    private Button signin;
    private Animation partRotate;
    private ScrollView scrollView;
    private TextView mailtext, logoutstatus, verifytxt, nametext;
    private ImageButton verifybutt;
    private ImageView accountico;
    private Uri personImgUri;
    private String personname,personemail,personimguristring;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle recieveBundle = getArguments();
        if(recieveBundle!=null) {
            personname = getArguments().getString("name");
            personemail = getArguments().getString("mailid");
            personimguristring = getArguments().getString("photo");
            personImgUri = Uri.parse(personimguristring);
        }
        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        signin = root.findViewById(R.id.signinbuttabout);
        mailtext = root.findViewById(R.id.emailtext);
        nametext = root.findViewById(R.id.nametext);
        accountico = root.findViewById(R.id.accounticon);
        logoutstatus = root.findViewById(R.id.loggedouttext);
        scrollView = root.findViewById(R.id.loginscrollview);
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        verifybutt = root.findViewById(R.id.mailverifybtn);
        verifytxt = root.findViewById(R.id.verifystatustxt);

        partRotate = AnimationUtils.loadAnimation(getActivity(), R.anim.ding_dong_ding);
        partRotate.setFillAfter(true);
        accountico.startAnimation(partRotate);
        if(personemail!=null||personname !=null||personimguristring!=null){
            logoutstatus.setVisibility(View.GONE);
            signin.setVisibility(View.GONE);
        }
        if(personemail!=null)
        {   mailtext.setText(personemail);}
        if(personname !=null) {
            nametext.setText(personname);
        }
        if(personimguristring!=null){
            Picasso.get().load(personImgUri).into(accountico);
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), personImgUri);
                accountico.setImageBitmap(bitmap);
                accountico.clearAnimation();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                Log.println(1,"ioexeption",e.getMessage());
            }
        }
        // Check for existing Google Sign In account, if the user is already signed in
       // the GoogleSignInAccount will be non-null.
        if(personemail!=null&&personname !=null&&personimguristring!=null) {
            Gaccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        }
        if (user != null) {
            logoutstatus.setVisibility(View.GONE);
            signin.setVisibility(View.GONE);
            signedin(user);
        } else if( Gaccount!=null){
            logoutstatus.setVisibility(View.GONE);
            signin.setVisibility(View.GONE);
            signedinwgoogle(Gaccount);
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
        checkIfEmailVerified(text);
    }

    private void signedinwgoogle(GoogleSignInAccount googleSignInAccount){
        String email = googleSignInAccount.getEmail();
        String dispname = googleSignInAccount.getDisplayName();
        Uri accimguri = googleSignInAccount.getPhotoUrl();
        try{
            Bitmap accimg =MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), accimguri);
            accountico.setImageBitmap(accimg);
            accountico.clearAnimation();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            Log.println(1,"ioexeption",e.getMessage());
        }
        mailtext.setText(email);
        mailtext.setTextColor(getResources().getColor(R.color.green));
        nametext.setText(dispname);
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

    private void checkIfEmailVerified(final String address)
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (Objects.requireNonNull(user).isEmailVerified())
        {
            verifytxt.setText(R.string.verified_text);
            verifytxt.setTextColor(getResources().getColor(R.color.green));
            verifytxt.setTypeface(Typeface.DEFAULT_BOLD);
            verifybutt.setVisibility(View.INVISIBLE);
        }
        else
        {
            verifytxt.setText(R.string.unverified_verify_now);
            verifytxt.setTextColor(getResources().getColor(R.color.dark_red));
            verifytxt.setTypeface(Typeface.DEFAULT);
            verifybutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadDialogWhileLinkGen.show();
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(),"Link has been sent at "+address,Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(),"error",Toast.LENGTH_LONG).show();
                                    }
                                    loadDialogWhileLinkGen.dismiss();
                                }
                            });
                }
            });
        }
    }

}