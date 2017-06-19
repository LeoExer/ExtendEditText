package com.leo.extendedittext.span;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * Created by Leo on 2017/3/17.
 */

public class ExtendURLSpan extends URLSpan {

    private static final int DEFAULT_COLOR = Color.BLUE;
    private static final boolean DEFAULT_DRAW_UNDER_LINE = true;

    private int mColor;
    private boolean mDrawUnderLine;

    public ExtendURLSpan(String url) {
        super(url);
        this.mColor = DEFAULT_COLOR;
        this.mDrawUnderLine = DEFAULT_DRAW_UNDER_LINE;
    }

    public ExtendURLSpan(String url, int color, boolean drawUnderLine) {
        super(url);
        this.mColor = color == 0 ? DEFAULT_COLOR : color;
        this.mDrawUnderLine = drawUnderLine;
    }

    public ExtendURLSpan(Parcel src) {
        super(src);
        this.mColor = src.readInt();
        this.mDrawUnderLine = src.readInt() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mColor);
        dest.writeInt(mDrawUnderLine ? 1 : 0);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mColor);
        ds.setUnderlineText(mDrawUnderLine);
    }
}
