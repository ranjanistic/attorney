package com.app.summaryzer;

import android.graphics.drawable.Drawable;

public interface OnOptionChosenListener {
    void onChoice(int choice);
    String onCallHeader();
    Drawable onCallImage();
}
