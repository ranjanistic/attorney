package com.app.summaryzer;

import android.graphics.drawable.Drawable;

public interface OnDialogTextListener {
    void onApply(String text);
    String onCallText();
    String onCallSubText();
    String onCallPos();
    String onCallNeg();
    Drawable onCallImg();
}
