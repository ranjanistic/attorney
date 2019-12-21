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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import java.util.Objects;

public class CustomAlertDialog extends AppCompatDialog {
    private OnDialogAlertListener onDialogAlertListener;

    public CustomAlertDialog(Context context, OnDialogAlertListener onDialogAlertListener) {
        super(context);
        this.onDialogAlertListener = onDialogAlertListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert_dialog);
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView alcont = findViewById(R.id.alert_content);
        final TextView alsubcont = findViewById(R.id.alert_subcontent);
        final ImageView alimg = findViewById(R.id.alert_image);
        final Button dismiss = findViewById(R.id.alert_dismiss_btn);

        String head = onDialogAlertListener.onCallText();
        String subhead  = onDialogAlertListener.onCallSub();
        Drawable image = onDialogAlertListener.onCallImg();

        assert alcont != null;
        alcont.setText(head);
        assert alsubcont != null;
        alsubcont.setText(subhead);
        assert alimg != null;
        alimg.setImageDrawable(image);

        assert dismiss != null;
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}