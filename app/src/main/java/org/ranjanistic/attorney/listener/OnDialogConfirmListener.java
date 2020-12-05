package org.ranjanistic.attorney.listener;

import android.graphics.drawable.Drawable;

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
