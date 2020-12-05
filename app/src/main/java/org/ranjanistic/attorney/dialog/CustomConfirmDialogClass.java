package org.ranjanistic.attorney.dialog;

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

import org.ranjanistic.attorney.R;
import org.ranjanistic.attorney.listener.OnDialogConfirmListener;

import java.util.Objects;

public class CustomConfirmDialogClass extends AppCompatDialog {
    private OnDialogConfirmListener onDialogConfirmListener;
    public CustomConfirmDialogClass(Context context, OnDialogConfirmListener onDialogConfirmListener){
        super(context);
        this.onDialogConfirmListener = onDialogConfirmListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_confirm_dialog);
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView diacont = findViewById(R.id.dialog_content);
        final TextView diasubcont = findViewById(R.id.dialog_subcontent);
        final ImageView diaimg = findViewById(R.id.dialog_image);
        final Button dianeg = findViewById(R.id.btn_no);
        final Button diapos = findViewById(R.id.btn_yes);

        assert diacont != null;
        assert diasubcont != null;
        assert diaimg != null;
        assert diapos != null;
        assert dianeg != null;
        String head = onDialogConfirmListener.onCallText();
        String subhead = onDialogConfirmListener.onCallSub();
        String accept = onDialogConfirmListener.onCallPos();
        String reject = onDialogConfirmListener.onCallNeg();
        Drawable image = onDialogConfirmListener.onCallImg();
    //    int poscol = onDialogConfirmListener.onCallPoscol();
        //int negcol = onDialogConfirmListener.onCallNegcol();

        diacont.setText(head);
        diasubcont.setText(subhead);
        dianeg.setText(reject);
        //dianeg.getBackground().setColorFilter(ContextCompat.getColor(getContext(), negcol), PorterDuff.Mode.MULTIPLY);
        diapos.setText(accept);
        //diapos.getBackground().setColorFilter(ContextCompat.getColor(getContext(), poscol), PorterDuff.Mode.MULTIPLY);
        diaimg.setImageDrawable(image);


        dianeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        diapos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDialogConfirmListener.onApply(true);
                    dismiss();
            }
        });
    }
}
