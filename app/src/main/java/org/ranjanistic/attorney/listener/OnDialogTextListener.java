package org.ranjanistic.attorney.listener;

import android.graphics.drawable.Drawable;

public interface OnDialogTextListener {
    void onApply(String text);
    String onCallText();
    String onCallSubText();
    String onCallPos();
    String onCallNeg();
    String onCallHint();
    Drawable onCallImg();
}
