package com.leo.extendedittext.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.BulletSpan;

/**
 * Created by Leo on 2017/3/17.
 */

public class ExtendBulletSpan extends BulletSpan {

    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_RADIUS = 3;
    private static final int DEFAULT_GAP_WIDTH = 2;

    private int mColor;
    private int mRadius;
    private int mGapWidth;

    private Path sBulletPath = null;

    public ExtendBulletSpan() {
        this.mColor = DEFAULT_COLOR;
        this.mRadius = DEFAULT_RADIUS;
        this.mGapWidth = DEFAULT_GAP_WIDTH;
    }

    public ExtendBulletSpan(int color, int radius, int gapWidth) {
        this.mColor = color == 0 ? DEFAULT_COLOR : color;
        this.mRadius = radius == 0 ? DEFAULT_RADIUS : radius;
        this.mGapWidth = gapWidth == 0 ? DEFAULT_GAP_WIDTH : gapWidth;
    }

    public ExtendBulletSpan(Parcel src) {
        super(src);
        this.mColor = src.readInt();
        this.mRadius = src.readInt();
        this.mGapWidth = src.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mColor);
        dest.writeInt(mRadius);
        dest.writeInt(mGapWidth);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        // 获取间隔宽度
        return 2 * mRadius + mGapWidth;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        // 绘制, 修改颜色和间隔宽度
        // copy from superclass
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();
            int oldColor = p.getColor();

            p.setColor(mColor);
            p.setStyle(Paint.Style.FILL);

            if (c.isHardwareAccelerated()) {
                if (sBulletPath == null) {
                    sBulletPath = new Path();
                    // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                    sBulletPath.addCircle(0.0f, 0.0f, 1.2f * mRadius, Path.Direction.CW);
                }

                c.save();
                c.translate(x + dir * mRadius, (top + bottom) / 2.0f);
                c.drawPath(sBulletPath, p);
                c.restore();
            } else {
                c.drawCircle(x + dir * mRadius, (top + bottom) / 2.0f, mRadius, p);
            }

            p.setColor(oldColor);
            p.setStyle(style);
        }
    }
}
