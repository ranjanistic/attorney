package com.app.summaryzer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class CustomOnOptListener extends AppCompatDialog {
    private OnOptionChosenListener onOptionChosenListener;
    private  Button set, cancel;
    private RadioButton r101, r102, r103;
    public CustomOnOptListener(Context context, OnOptionChosenListener onOptionChosenListener) {
        super(context);
        this.onOptionChosenListener = onOptionChosenListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_radio_choice_dialog);
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView choiceimg;
        TextView choiceHead;
        RadioGroup radioGroup;
        set = findViewById(R.id.choice_submit);
        cancel = findViewById(R.id.choice_cancel);
        choiceimg = findViewById(R.id.choice_dialog_image);
        choiceHead = findViewById(R.id.choice_dialog_head);
        radioGroup = findViewById(R.id.radiogroup);
        Drawable choiceimgreceived = onOptionChosenListener.onCallImage();
        String headingtext = onOptionChosenListener.onCallHeader();
        if (choiceHead != null) {
            choiceHead.setText(headingtext);
        }
        if (choiceimg != null) {
            choiceimg.setImageDrawable(choiceimgreceived);
        }
        r103 = findViewById(R.id.radio3);
        r102 = findViewById(R.id.radio2);
        r101 = findViewById(R.id.radio1);
        if(getThemeStatus()==101) {r101.setChecked(true);}
        else if(getThemeStatus()==102){r102.setChecked(true);}
        else if(getThemeStatus()==103){r103.setChecked(true);}
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if (radioGroup != null) {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, final int radioid) {
                    set.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (radioid) {
                                case R.id.radio1:
                                    onOptionChosenListener.onChoice(101);
                                    break;
                                case R.id.radio2:
                                    onOptionChosenListener.onChoice(102);
                                    break;
                                case R.id.radio3:
                                    onOptionChosenListener.onChoice(103);
                                    break;
                            }
                            dismiss();
                        }
                    });
                }
            });
        }
    }

    private int getThemeStatus() {
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences("theme", MODE_PRIVATE);
        return mSharedPreferences.getInt("themeCode", 0);
    }
}
