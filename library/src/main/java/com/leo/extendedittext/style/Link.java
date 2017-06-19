package com.leo.extendedittext.style;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.URLSpan;

import com.leo.extendedittext.span.ExtendURLSpan;

/**
 * Created by Leo on 2017/3/17.
 */

public class Link extends Style {

    private int mColor;
    private boolean mDrawUnderLine;

    @Override
    public void set(Editable text, int start, int end) {
        if (start >= end) {
            return;
        }

        text.setSpan(new ExtendURLSpan(text.toString(), mColor, mDrawUnderLine),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 只影响选择文本
    }

    @Override
    public void remove(Editable text, int start, int end) {
        if (start >= end) {
            return;
        }

        // 这里无需恢复未选中文本, 因为link格式是完整才有效的
        URLSpan[] spans = text.getSpans(start, end, URLSpan.class);
        for (URLSpan span : spans) {
            text.removeSpan(span);
        }
    }

    @Override
    public boolean isSetting(Editable text, int start, int end) {
        if (start >= end) {
            return false;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (text.getSpans(i, i + 1, URLSpan.class).length > 0) {
                builder.append(text.subSequence(i, i + 1).toString());
            }
        }

        return text.subSequence(start, end).toString().equals(builder.toString());
    }

    public Link setColor(int color) {
        this.mColor = color;
        return this;
    }

    public Link drawUnderLine(boolean draw) {
        this.mDrawUnderLine = draw;
        return this;
    }
}
