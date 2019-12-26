package com.app.summaryzer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import java.util.Objects;

public class CustomOnOptListener extends AppCompatDialog {
    private OnOptionChosenListener onOptionChosenListener;
    private  Button set, cancel;


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
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancel();
                        }
                    });
                }
            });
        }
    }
}
