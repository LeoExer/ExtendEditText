package com.leo.extendedittext.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.style.QuoteSpan;

/**
 * Created by Leo on 2017/3/17.
 */

public class ExtendQuoteSpan extends QuoteSpan {
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_STRIPE_WIDTH = 2;
    private static final int DEFUALT_GAP_WIDTH = 2;

    private int mColor;
    private int mStripeWidth;
    private int mGapWidth;

    public ExtendQuoteSpan() {
        this.mColor = DEFAULT_COLOR;
        this.mStripeWidth = DEFAULT_STRIPE_WIDTH;
        this.mGapWidth = DEFUALT_GAP_WIDTH;
    }

    public ExtendQuoteSpan(int color, int stripeWidth, int gapWidth) {
        this.mColor = color == 0 ? DEFAULT_COLOR : color;
        this.mStripeWidth = stripeWidth == 0 ? DEFAULT_STRIPE_WIDTH : stripeWidth;
        this.mGapWidth = gapWidth == 0 ? DEFUALT_GAP_WIDTH : gapWidth;
    }

    public ExtendQuoteSpan(Parcel src) {
        super(src);
        mColor = src.readInt();
        mStripeWidth = src.readInt();
        mGapWidth = src.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mColor);
        dest.writeInt(mStripeWidth);
        dest.writeInt(mGapWidth);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return mStripeWidth + mGapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout layout) {
        Paint.Style style = p.getStyle();
        int color = p.getColor();

        p.setStyle(Paint.Style.FILL);
        p.setColor(mColor);

        c.drawRect(x, top, x + dir * mStripeWidth, bottom, p);

        p.setStyle(style);
        p.setColor(color);
    }
}
