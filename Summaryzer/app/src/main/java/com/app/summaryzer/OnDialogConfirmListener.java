package com.app.summaryzer;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Button;

public interface OnDialogConfirmListener {
    void onApply(Boolean confirm);
    String onCallText();
    String onCallSub();
    String onCallPos();
    String onCallNeg();
    Drawable onCallImg();
    //int onCallPoscol();
    //int onCallNegcol();
}
