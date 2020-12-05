package org.ranjanistic.attorney.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;

import com.app.summaryzer.R;
import org.ranjanistic.attorney.listener.OnDialogTextListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class CustomTextDialog extends AppCompatDialog {
    private OnDialogTextListener onDialogTextListener;
    public CustomTextDialog(Context context, OnDialogTextListener onDialogTextListener){
        super(context);
        this.onDialogTextListener = onDialogTextListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_text_dialog);
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView textDiaHead = findViewById(R.id.text_dialog_head);
        final TextView textDiaSubhead = findViewById(R.id.text_dialog_subhead);
        final ImageView diaimg = findViewById(R.id.text_dialog_image);
        final Button textCancel = findViewById(R.id.text_cancel);
        final Button textSubmit = findViewById(R.id.text_submit);
        TextInputLayout texthint = findViewById(R.id.dialog_text_hint);
        assert textDiaHead != null;
        assert textDiaSubhead != null;
        assert diaimg != null;
        assert textSubmit != null;
        assert textCancel != null;
        String head = onDialogTextListener.onCallText();
        String subhead = onDialogTextListener.onCallSubText();
        String accept = onDialogTextListener.onCallPos();
        String reject = onDialogTextListener.onCallNeg();
        String hint = onDialogTextListener.onCallHint();
        Drawable image = onDialogTextListener.onCallImg();
        final EditText text = findViewById(R.id.dialog_text);
        //    int poscol = onDialogTextListener.onCallPoscol();
        //int negcol = onDialogTextListener.onCallNegcol();
        if (texthint != null) {
            texthint.setHint(hint);
        }
        textDiaHead.setText(head);
        textDiaSubhead.setText(subhead);
        textCancel.setText(reject);
        //textCancel.getBackground().setColorFilter(ContextCompat.getColor(getContext(), negcol), PorterDuff.Mode.MULTIPLY);
        textSubmit.setText(accept);
        //textSubmit.getBackground().setColorFilter(ContextCompat.getColor(getContext(), poscol), PorterDuff.Mode.MULTIPLY);
        diaimg.setImageDrawable(image);


        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        textSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entryTxt = text.getText().toString();
                if ( TextUtils.isEmpty(entryTxt)){
                    Toast.makeText(getContext(),"This field can\'t be empty.",Toast.LENGTH_LONG).show();
                } else{
                    onDialogTextListener.onApply(entryTxt);
                    dismiss();
                }
            }
        });
    }
}
