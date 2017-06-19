package com.leo.extendedittext.style;

import android.text.Editable;
import android.text.Spanned;

import com.leo.extendedittext.Rule;

/**
 * Created by Leo on 2017/3/17.
 */

public abstract class Style {

    protected int mRule = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

    private boolean mCover = false;

    /**
     * 改变选中文本样式
     * @param text 选中的可编辑文本
     * @param start 开始索引
     * @param end 结束索引
     * @param rule 规则
     * @return 若设置样式返回true, 清除样式返回false
     */
    public boolean format(Editable text, int start, int end, Rule rule) {
        convertRule(rule);

        boolean result = false;
        if (!isSetting(text, start, end)) {
            set(text, start, end);
            result = true;
        } else {
            if (!mCover) {
                remove(text, start, end);
            }
        }

        return result;
    }

    /**
     * 设置样式
     * @param text 可编辑文本
     * @param start 开始索引
     * @param end 结束索引
     */
    public abstract void set(Editable text, int start, int end);

    /**
     * 移除样式
     * @param text 可编辑文本
     * @param start 开始索引
     * @param end 结束索引
     */
    public abstract void remove(Editable text,int start, int end);

    /**
     * 选中文本是否已设置样式
     * @param text 可编辑文本
     * @param start 开始索引
     * @param end 结束索引
     * @return 若选中的全部文本已设置该样式, 返回true; 反之, 返回false.
     */
    public abstract boolean isSetting(Editable text, int start, int end);

    /**
     * 是否覆盖设置样式
     */
    public void setCover(boolean cover) {
        this.mCover = cover;
    }

    private void convertRule(Rule rule) {
        switch (rule) {
            case EXCLUSIVE_INCLUSIVE:
                mRule = Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
                break;
            case INCLUSIVE_EXCLUSIVE:
                mRule = Spanned.SPAN_INCLUSIVE_EXCLUSIVE;
                break;
            case INCLUSIVE_INCLUSIVE:
                mRule = Spanned.SPAN_INCLUSIVE_INCLUSIVE;
                break;
            default:
                mRule = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
                break;
        }
    }
}
