package org.ranjanistic.attorney.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatDialog;

import org.ranjanistic.attorney.R;
import org.ranjanistic.attorney.listener.OnProgressLoadListener;

import java.util.Objects;

public class CustomProgressDialog extends AppCompatDialog {
    private OnProgressLoadListener onProgressLoadListener;
    public CustomProgressDialog(Context context, OnProgressLoadListener onProgressLoadListener){
        super(context);
        this.onProgressLoadListener = onProgressLoadListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_progress_dialog);
        this.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        onProgressLoadListener.onStart();
    }
}
