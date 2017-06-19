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

public class Bold extends Style {

    @Override
    public void set(Editable text, int start, int end) {
        if (start >= end) {
            return;
        }

        text.setSpan(new StyleSpan(Typeface.BOLD), start, end, mRule);
    }

    @Override
    public void remove(Editable text, int start, int end) {
        if (start >= end) {
            return;
        }

        StyleSpan[] spans = text.getSpans(start, end, StyleSpan.class);
        List<TypeBean> list = new ArrayList<>(spans.length);
        for (StyleSpan span : spans) {
            if (span.getStyle() == Typeface.BOLD) {
                list.add(new TypeBean(text.getSpanStart(span), text.getSpanEnd(span)));
                text.removeSpan(span); // remove
            }
        }

        // 恢复未选上但与移除文本具有相同样式的文本
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

        // 思路: 遍历可编辑文本, 若选中文本存在未设置该样式的, 返回false; 反之, 返回true
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            // 获取每个字符的样式, 可能有重复, 只需获取判断一次
            StyleSpan[] spans = text.getSpans(i, i + 1, StyleSpan.class);
            for (StyleSpan span : spans) {
                if (span.getStyle() == Typeface.BOLD) {
                    builder.append(text.subSequence(i, i + 1).toString());
                    break;
                }
            }
        }

        return text.subSequence(start, end).toString().equals(builder.toString());
    }
}
