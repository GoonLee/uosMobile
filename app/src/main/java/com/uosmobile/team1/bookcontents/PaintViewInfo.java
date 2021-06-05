package com.uosmobile.team1.bookcontents;

import android.graphics.Bitmap;

public class PaintViewInfo {
    private Bitmap frontBitmap;
    private boolean backgroundToggled;

    public PaintViewInfo(Bitmap frontBitmap, boolean backgroundToggled) {
        this.frontBitmap = frontBitmap;
        this.backgroundToggled = backgroundToggled;
    }

    public Bitmap getFrontBitmap() {
        return frontBitmap;
    }

    public void setFrontBitmap(Bitmap frontBitmap) {
        this.frontBitmap = frontBitmap;
    }

    public boolean isBackgroundToggled() {
        return backgroundToggled;
    }

    public void setBackgroundToggled(boolean backgroundToggled) {
        this.backgroundToggled = backgroundToggled;
    }
}
