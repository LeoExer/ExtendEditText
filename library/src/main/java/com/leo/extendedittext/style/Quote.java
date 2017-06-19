package com.leo.extendedittext.style;

import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.QuoteSpan;

import com.leo.extendedittext.span.ExtendQuoteSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/3/17.
 */

public class Quote extends Style {

    private int mColor;
    private int mStripeWidth;
    private int mGapWidth;

    @Override
    public void set(Editable text, int selectStart, int selectEnd) {
        String[] lines = TextUtils.split(text.toString(), "\n");
        for (int i = 0; i < lines.length; i++) {
            // 若当前行已设置, 跳过
            if (isSettingOnLine(text, i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart += lines[j].length() + 1; // \n
            }

            if (lineStart > selectEnd) {
                break;
            }

            int lineEnd = lineStart + lines[i].length();

            if (isSelected(lineStart, lineEnd, selectStart, selectEnd)) {
                text.setSpan(new ExtendQuoteSpan(mColor, mStripeWidth, mGapWidth),
                        lineStart, lineEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    @Override
    public void remove(Editable text, int selectStart, int selectEnd) {
        String[] lines = TextUtils.split(text.toString(), "\n");
        for (int i = 0; i < lines.length; i++) {
            // 若当前行没设置, 跳过
            if (!isSettingOnLine(text, i)) {
                continue;
            }

            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart += lines[j].length() + 1; // \n
            }

            if (lineStart > selectEnd) {
                break;
            }

            int lineEnd = lineStart + lines[i].length();

            if (isSelected(lineStart, lineEnd, selectStart, selectEnd)) {
                QuoteSpan[] spans = text.getSpans(lineStart, lineEnd, QuoteSpan.class);
                for (QuoteSpan span : spans) {
                    text.removeSpan(span);
                }
            }
        }
    }

    @Override
    public boolean isSetting(Editable text, int selectStart, int selectEnd) {
        String[] lines = TextUtils.split(text.toString(), "\n");
        List<Integer> targetLines = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            // 获取每行的首字符和尾字符索引
            int lineStart = 0;
            for (int j = 0; j < i; j++) {
                lineStart += lines[j].length() + 1; // \n
            }
            // 选择文本的结束索引小于当前行的首索引, 表示当前行已不再选择范围内, 跳出循环
            if (lineStart > selectEnd) {
                break;
            }

            int lineEnd = lineStart + lines[i].length();
            if (isSelected(lineStart, lineEnd, selectStart, selectEnd)) {
                targetLines.add(i);
            }
        }

        for (int index : targetLines) {
            if (!isSettingOnLine(text, index)) {
                return false;
            }
        }

        return true;
    }

    private boolean isSettingOnLine(Editable text, int lineIndex) {
        String[] lines = TextUtils.split(text.toString(), "\n");
        if (lineIndex < 0 || lineIndex >= lines.length) {
            return false;
        }

        // 获取当前行的首字符和尾字符索引
        int lineStart = 0;
        for (int i = 0; i < lineIndex; i++) {
            lineStart += lines[i].length() + 1; // +1: \n
        }
        int lineEnd = lineStart + lines[lineIndex].length();

        return text.getSpans(lineStart, lineEnd, QuoteSpan.class).length > 0;
    }

    /**
     * 四种情况代表当前行已被选择<P>
     * 1. selectStart > lineStart && selectEnd < lineEnd <br>
     * 2. selectStart > lineStart && selectEnd > lineEnd <br>
     * 3. selectStart < lineStart && selectEnd < lineEnd <br>
     * 4. selectStart < lineStart && selectEnd > lineEnd <br>
     *
     * @param lineStart 当前行首字符索引
     * @param lineEnd 当前行尾字符索引
     * @param selectStart 选择文本首字符索引
     * @param selectEnd 选择文本 尾字符索引
     * @return 满足上面四种情况返回true; 否则, 返回false
     */
    private boolean isSelected(int lineStart, int lineEnd, int selectStart, int selectEnd) {
        return selectStart <= lineEnd && selectEnd >= lineStart;
    }

    public Quote setColor(int color) {
        this.mColor = color;
        return this;
    }

    public Quote setStripeWidth(int stripeWidth) {
        this.mStripeWidth = stripeWidth;
        return this;
    }

    public Quote setGapWidth(int gapWidth) {
        this.mGapWidth = gapWidth;
        return this;
    }
}
