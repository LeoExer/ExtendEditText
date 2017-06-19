package com.leo.extendedittext.style;

import android.text.Editable;
import android.text.style.UnderlineSpan;

import com.leo.extendedittext.TypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/3/17.
 */

public class Underline extends Style {

    @Override
    public void set(Editable text, int start, int end) {
        if (start >= end) {
            return;
        }

        text.setSpan(new UnderlineSpan(), start, end, mRule);
    }

    @Override
    public void remove(Editable text, int start, int end) {
        if (start >= end) {
            return;
        }

        UnderlineSpan[] spans = text.getSpans(start, end, UnderlineSpan.class);
        List<TypeBean> list = new ArrayList<>(spans.length);
        for (UnderlineSpan span : spans) {
            list.add(new TypeBean(text.getSpanStart(span), text.getSpanEnd(span)));
            text.removeSpan(span);
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
            if (text.getSpans(i, i + 1, UnderlineSpan.class).length > 0) {
                builder.append(text.subSequence(i, i + 1).toString());
            }
        }

        return text.subSequence(start, end).toString().equals(builder.toString());
    }
}
