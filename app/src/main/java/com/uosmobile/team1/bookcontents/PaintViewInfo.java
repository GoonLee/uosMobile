package com.uosmobile.team1.bookcontents;

import android.graphics.Bitmap;

/**
 * PaintView에서 DB를 통해 Read/Write할 정보를 필드로 갖는 Immutable 객체입니다.
 */
public class PaintViewInfo {
    private final Bitmap frontBitmap;
    private final boolean backgroundToggled;

    public PaintViewInfo(Bitmap frontBitmap, boolean backgroundToggled) {
        this.frontBitmap = frontBitmap;
        this.backgroundToggled = backgroundToggled;
    }

    public Bitmap getFrontBitmap() {
        return frontBitmap;
    }

    public boolean isBackgroundToggled() {
        return backgroundToggled;
    }
}
