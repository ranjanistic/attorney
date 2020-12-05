package org.ranjanistic.attorney.ui.About;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.ranjanistic.attorney.R;

public class AboutFragment extends Fragment {

    private AboutViewModel aboutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutViewModel =
               ViewModelProviders.of(this).get(AboutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about, container, false);

    /*    final TextView textView = root.findViewById(R.id.navigation_about);
        aboutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        ImageButton updatebutt,ratebutt,bugbutt,feedbutt,gitbutt,sharebutt;
        updatebutt = root.findViewById(R.id.updatebtn);
        ratebutt = root.findViewById(R.id.ratebtn);
        bugbutt = root.findViewById(R.id.bugreportbtn);
        feedbutt = root.findViewById(R.id.feedbackbtn);
        gitbutt = root.findViewById(R.id.githubbtn);
        sharebutt = root.findViewById(R.id.shareappbtn);

        updatebutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"App Unreleased.", Toast.LENGTH_SHORT).show();
            }
        });
        ratebutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=null");
                Intent intmail = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intmail);

            }
        });
        bugbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"App Unreleased.", Toast.LENGTH_SHORT).show();
            }
        });
        feedbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("mailto:priyanshuranjan88@gmail.com");
                Intent intmail = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intmail);
            }
        });
        gitbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://ranjanistic.github.io/summarizer-web/summarizer.html");
                Intent intmail = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intmail);
            }
        });
        sharebutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey! Try this app";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Summaryzer");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share with humans via"));
            }
        });
        return root;

    }
}