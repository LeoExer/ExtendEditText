package com.leo.extendedittext.style;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.style.StyleSpan;

import com.leo.extendedittext.TypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/3/17.
 */

public class Italic extends Style {

    @Override
    public void set(Editable text, int start, int end) {
        if (start >= end) {
            return;
        }

        text.setSpan(new StyleSpan(Typeface.ITALIC), start, end, mRule);
    }

    @Override
    public void remove(Editable text, int start, int end) {
        if (start >= end) {
            return;
        }

        StyleSpan[] spans = text.getSpans(start, end, StyleSpan.class);
        List<TypeBean> list = new ArrayList<>(spans.length);
        for (StyleSpan span : spans) {
            if (span.getStyle() == Typeface.ITALIC) {
                list.add(new TypeBean(text.getSpanStart(span), text.getSpanEnd(span)));
                text.removeSpan(span); // remove
            }
        }

        for (TypeBean bean : list) {
            if (bean.isValid()) {
                if (bean.getStart() < start) {
                    set(text, bean.getStart(), start);
                }

                if (bean.getEnd() > end) {
                    set(text, end, bean.getEnd());
                }
            }
        }
    }

    @Override
    public boolean isSetting(Editable text, int start, int end) {
        if (start >= end) {
            return false;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            StyleSpan[] spans = text.getSpans(i, i + 1, StyleSpan.class);
            for (StyleSpan span : spans) {
                if (span.getStyle() == Typeface.ITALIC) {
                    builder.append(text.subSequence(i, i + 1).toString());
                    break;
                }
            }
        }

        return text.subSequence(start, end).toString().equals(builder.toString());
    }
}
