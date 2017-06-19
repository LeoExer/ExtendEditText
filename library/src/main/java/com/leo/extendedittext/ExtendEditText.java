package com.leo.extendedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.leo.extendedittext.style.Bold;
import com.leo.extendedittext.style.Bullet;
import com.leo.extendedittext.style.Italic;
import com.leo.extendedittext.style.Link;
import com.leo.extendedittext.style.Quote;
import com.leo.extendedittext.style.Strikethrough;
import com.leo.extendedittext.style.Style;
import com.leo.extendedittext.style.Underline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于EditText扩展的文本编辑器
 *
 * Created by Leo on 2017/3/17.
 */

public class ExtendEditText extends EditText implements TextWatcher {

    // style
    public static final int STYLE_BOLD = 0x0000; // 粗体
    public static final int STYLE_ITALIC = 0x0001; // 斜体
    public static final int STYLE_UNDERLINE = 0x0002; // 下划线
    public static final int STYLE_STRIKETHROUGH = 0x0003; // 中划线
    public static final int STYLE_BULLET = 0x0004; // 着重号
    public static final int STYLE_QUOTE = 0x0005; // 引用
    public static final int STYLE_LINK = 0x0006; // 链接

    // rule
    private static final Rule[] mRules = {
            Rule.EXCLUSIVE_EXCLUSIVE,
            Rule.EXCLUSIVE_INCLUSIVE,
            Rule.INCLUSIVE_EXCLUSIVE,
            Rule.INCLUSIVE_INCLUSIVE
    };
    private Rule mRule;

    // cover
    private boolean mIsCover;
    private List<Integer> mCoverStyles;

    // Bullet
    private int mBulletColor;
    private int mBulletRadius;
    private int mBulletGapWidth;

    // Quote
    private int mQuoteColor;
    private int mQuoteStripeWidth;
    private int mQuoteGapWidth;

    // Link
    private int mLinkColor;
    private boolean mDrawLinkUnderline;

    // history
    private List<SpannableStringBuilder> mHistoryList; // 历史记录队列
    private List<Integer> mHistoryCursorIndexs; // 光标索引
    private boolean mEnableHistory;
    private int mHistoryIndex;
    private AtomicBoolean mIsHistoryWorking;
    private int mSaveHistoryCapacity;
    private SpannableStringBuilder mBeforeInput;
    private SpannableStringBuilder mLastInput;
    private int mBeforeCursorIndex;
    private int mLastCursorIndex;

    // Listener
    private ExtendEditTextListener mListener;

    public ExtendEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ExtendEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExtendEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mIsCover = false;
        mCoverStyles = new LinkedList<>();

        mHistoryList = new LinkedList<>();
        mHistoryCursorIndexs = new LinkedList<>();
        mIsHistoryWorking = new AtomicBoolean(false);

        if (context != null && attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExtendEditText);

            int index = ta.getInt(R.styleable.ExtendEditText_rule, 0);
            setRule(mRules[index]);

            setBulletColor(ta.getColor(R.styleable.ExtendEditText_bulletColor, Color.BLACK));
            setBulletRadius(ta.getDimensionPixelSize(R.styleable.ExtendEditText_bulletRadius, 0));
            setBulletGapWidth(ta.getDimensionPixelSize(R.styleable.ExtendEditText_bulletGapWidth, 0));

            setQuoteColor(ta.getColor(R.styleable.ExtendEditText_quoteColor, Color.BLACK));
            setQuoteStripeWidth(ta.getDimensionPixelSize(R.styleable.ExtendEditText_quoteStripeWidth, 0));
            setQuoteGapWidth(ta.getDimensionPixelSize(R.styleable.ExtendEditText_quoteGapWidth, 0));

            setLinkColor(ta.getColor(R.styleable.ExtendEditText_linkColor, Color.BLUE));
            drawLinkUnderLink(ta.getBoolean(R.styleable.ExtendEditText_drawUnderLine, true));

            enableHistory(ta.getBoolean(R.styleable.ExtendEditText_enableHistory, true));
            setHistoryCapacity(ta.getInt(R.styleable.ExtendEditText_historyCapacity, 50));

            ta.recycle();
        }
    }

    /* ============ 配置 ============ */

    public void setExtendEditTextListener(ExtendEditTextListener listener) {
        this.mListener = listener;
    }

    public ExtendEditText setRule(Rule rule) {
        this.mRule = rule;
        return this;
    }

    public ExtendEditText setBulletColor(int color) {
        this.mBulletColor = color;
        return this;
    }

    public ExtendEditText setBulletRadius(int radius) {
        this.mBulletRadius = radius;
        return this;
    }

    public ExtendEditText setBulletGapWidth(int gapWidth) {
        this.mBulletGapWidth = gapWidth;
        return this;
    }

    public ExtendEditText setQuoteColor(int color) {
        this.mQuoteColor = color;
        return this;
    }

    public ExtendEditText setQuoteStripeWidth(int stripeWidth) {
        this.mQuoteStripeWidth = stripeWidth;
        return this;
    }

    public ExtendEditText setQuoteGapWidth(int gapWidth) {
        this.mQuoteGapWidth = gapWidth;
        return this;
    }

    public ExtendEditText setLinkColor(int color) {
        this.mLinkColor = color;
        return this;
    }

    public ExtendEditText drawLinkUnderLink(boolean draw) {
        this.mDrawLinkUnderline = draw;
        return this;
    }

    public ExtendEditText enableHistory(boolean enable) {
        this.mEnableHistory = enable;
        return this;
    }

    public ExtendEditText setHistoryCapacity(int capacity) {
        this.mSaveHistoryCapacity = capacity;
        return this;
    }

    /**
     * 获取所选字符的样式
     *
     * @return 返回全部样式; 如果没有, 则返回null
     */
    public List<Integer> getSelectionStyles(int start, int end) {
        if (start == 0 && end == 0) {
            return null;
        }

        int finalStart = start;
        int finalEnd = end;
        if (finalStart == finalEnd) {
            finalStart = start - 1;
        }
        List<Integer> styles = new ArrayList<>();

        Bold bold = new Bold();
        if(bold.isSetting(getEditableText(), finalStart, finalEnd)) {
            styles.add(STYLE_BOLD);
        }

        Italic italic = new Italic();
        if (italic.isSetting(getEditableText(), finalStart, finalEnd)) {
            styles.add(STYLE_ITALIC);
        }

        Underline underline = new Underline();
        if (underline.isSetting(getEditableText(), finalStart, finalEnd)) {
            styles.add(STYLE_UNDERLINE);
        }

        Strikethrough strikethrough = new Strikethrough();
        if (strikethrough.isSetting(getEditableText(), finalStart, finalEnd)) {
            styles.add(STYLE_STRIKETHROUGH);
        }

        Link link = new Link();
        if (link.isSetting(getEditableText(), finalStart, finalEnd)) {
            styles.add(STYLE_LINK);
        }

//        Bullet bullet = new Bullet();
//        if (bullet.isSetting(getEditableText(), finalStart, finalEnd)) {
//            styles.add(STYLE_BULLET);
//        }
//
//        Quote quote = new Quote();
//        if (quote.isSetting(getEditableText(), finalStart, finalEnd)) {
//            styles.add(STYLE_QUOTE);
//        }

        return styles;
    }

    /* ============ 设置样式 ============ */

    /**
     * 设置所选字符的字体格式为粗体
     */
    public ExtendEditText bold() {
        if (!mIsCover) {
            format(STYLE_BOLD);
        } else {
            mCoverStyles.add(STYLE_BOLD);
        }

        return this;
    }

    /**
     * 设置所选字符的字体格式为斜体
     */
    public ExtendEditText italic() {
        if (!mIsCover) {
            format(STYLE_ITALIC);
        } else {
            mCoverStyles.add(STYLE_ITALIC);
        }

        return this;
    }

    /**
     * 设置所选字符的字体格式为下划线
     */
    public ExtendEditText underline() {
        if (!mIsCover) {
            format(STYLE_UNDERLINE);
        } else {
            mCoverStyles.add(STYLE_UNDERLINE);
        }

        return this;
    }

    /**
     * 设置所选字符的字体格式为删除线
     */
    public ExtendEditText strikethrough() {
        if (!mIsCover) {
            format(STYLE_STRIKETHROUGH);
        } else {
            mCoverStyles.add(STYLE_STRIKETHROUGH);
        }

        return this;
    }

    /**
     * 设置所选字符的字体格式为链接
     */
    public ExtendEditText link() {
        if (!mIsCover) {
            format(STYLE_LINK);
        } else {
            mCoverStyles.add(STYLE_LINK);
        }

        return this;
    }

    /**
     * 设置所选段落前加重点符合
     */
    public ExtendEditText bullet() {
        format(STYLE_BULLET);
        return this;
    }

    /**
     * 设置所选段落前加引用符合
     */
    public ExtendEditText quote() {
        format(STYLE_QUOTE);
        return this;
    }

    /**
     * 清空所选字符所有格式
     */
    public void clear() {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        if (start >= end) {
            return;
        }

        mIsHistoryWorking.getAndSet(true);

        String normalText = getEditableText().toString().substring(start, end);
        getEditableText().delete(start, end);
        getEditableText().insert(start, normalText);
        // 非 Rule.EXCLUSIVE_EXCLUSIVE 首个或末尾无法清除格式
        clearSelectionStyles(start, start + 1);
        clearSelectionStyles(end - 1, end);
        addTextToHistory(getEditableText());
        onSelectionChanged(start, end);

        mIsHistoryWorking.getAndSet(false);
    }

    private boolean clearSelectionStyles(int start, int end) {
        boolean result = false;

        Bold bold = new Bold();
        if(bold.isSetting(getEditableText(), start, end)) {
            bold.remove(getEditableText(), start, end);
            result = true;
        }

        Italic italic = new Italic();
        if (italic.isSetting(getEditableText(), start, end)) {
            italic.remove(getEditableText(), start, end);
            result = true;
        }

        Underline underline = new Underline();
        if (underline.isSetting(getEditableText(), start, end)) {
            underline.remove(getEditableText(), start, end);
            result = true;
        }

        Strikethrough strikethrough = new Strikethrough();
        if (strikethrough.isSetting(getEditableText(), start, end)) {
            strikethrough.remove(getEditableText(), start, end);
            result = true;
        }

        Link link = new Link();
        if (link.isSetting(getEditableText(), start, end)) {
            link.remove(getEditableText(), start, end);
            result = true;
        }

        Bullet bullet = new Bullet();
        if (bullet.isSetting(getEditableText(), start, end)) {
            bullet.remove(getEditableText(), start, end);
            result = true;
        }

        Quote quote = new Quote();
        if (quote.isSetting(getEditableText(), start, end)) {
            quote.remove(getEditableText(), start, end);
            result = true;
        }

        return result;
    }

    public ExtendEditText cover() {
        mIsCover = true;
        return this;
    }

    public void action() {
        formatCover();
    }

    protected void format(int styleId) {
        Style style = createStyle(styleId);
        if (style != null) {
            if (getSelectionStart() >= getSelectionEnd() &&
                    (styleId != STYLE_BULLET && styleId != STYLE_QUOTE)) {
                return;
            }

            if (style.format(getEditableText(), getSelectionStart(), getSelectionEnd(), mRule)) {
                // 设置风格, 不会触发监听, 这里加入历史记录
                addTextToHistory(getEditableText());
            }
        }
    }

    protected void formatCover() {
        boolean hasSet = false;
        for (int i = 0; i < mCoverStyles.size(); i++) {
            int styleId = mCoverStyles.get(i);
            Style style = createStyle(styleId);
            if (style != null) {
                if (getSelectionStart() >= getSelectionEnd() &&
                        (styleId != STYLE_BULLET && styleId != STYLE_QUOTE)) {
                    return;
                }

                style.setCover(true);
                if (style.format(getEditableText(), getSelectionStart(), getSelectionEnd(), mRule)) {
                    hasSet = true;
                }
            }
        }
        if (hasSet) {
            // 设置风格, 不会触发监听, 这里加入历史记录
            addTextToHistory(getEditableText());
        }
        // clear type
        mCoverStyles.clear();
        mIsCover = false;
    }

    private Style createStyle(int styleId) {
        Style style;
        switch (styleId) {
            case STYLE_BOLD:
                style = new Bold();
                break;
            case STYLE_ITALIC:
                style = new Italic();
                break;
            case STYLE_UNDERLINE:
                style = new Underline();
                break;
            case STYLE_STRIKETHROUGH:
                style = new Strikethrough();
                break;
            case STYLE_BULLET:
                style = new Bullet();
                ((Bullet) style).setColor(mBulletColor)
                        .setRadius(mBulletRadius)
                        .setGapWidth(mBulletGapWidth);
                break;
            case STYLE_QUOTE:
                style = new Quote();
                ((Quote) style).setColor(mQuoteColor)
                        .setStripeWidth(mQuoteStripeWidth)
                        .setGapWidth(mQuoteGapWidth);
                break;
            case STYLE_LINK:
                style = new Link();
                ((Link) style).setColor(mLinkColor)
                        .drawUnderLine(mDrawLinkUnderline);
                break;
            default:
                style = null;
                break;
        }

        return style;
    }


    /* ============ 历史记录 ============ */

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addTextChangedListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!mEnableHistory || mIsHistoryWorking.get()) {
            return;
        }

        mBeforeInput = new SpannableStringBuilder(charSequence);
        mBeforeCursorIndex = getSelectionStart();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // ignore
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // 设置格式不会触发这里
        if (!mEnableHistory || mIsHistoryWorking.get()) {
            return;
        }

        // 若文本与输入前相同, 不保存
        if (editable != null && editable.toString().equals(mBeforeInput.toString())) {
            return;
        }

        mLastInput = new SpannableStringBuilder(editable);
        mLastCursorIndex = getSelectionStart();

        if (mHistoryList.size() >= mSaveHistoryCapacity) {
            mHistoryList.remove(0);
            mHistoryCursorIndexs.remove(0);
        }

        mHistoryList.add(mBeforeInput);
        mHistoryCursorIndexs.add(mBeforeCursorIndex);
        mHistoryIndex = mHistoryList.size();
    }

    public void redo() {
        if (!canRedo()) {
            return;
        }

        mIsHistoryWorking.getAndSet(true);

        if (mHistoryIndex >= mHistoryList.size() - 1) {
            mHistoryIndex = mHistoryList.size();
            if (mLastInput != null) {
                setText(mLastInput);
                setSelection(mLastCursorIndex);
            }
        } else {
            mHistoryIndex++;
            setText(mHistoryList.get(mHistoryIndex));
            setSelection(mHistoryCursorIndexs.get(mHistoryIndex));
        }

        mIsHistoryWorking.getAndSet(false);
    }

    public void undo() {
        if (!canUndo()) {
            return;
        }

        mIsHistoryWorking.getAndSet(true);

        mHistoryIndex--;
        setText(mHistoryList.get(mHistoryIndex));
        setSelection(mHistoryCursorIndexs.get(mHistoryIndex));

        mIsHistoryWorking.getAndSet(false);
    }

    private boolean canRedo() {
        return mEnableHistory // enable history
                && mSaveHistoryCapacity > 0 // capacity > 0
                && !mIsHistoryWorking.get() // history is not working
                && mHistoryList.size() > 0 // has history
                && mHistoryIndex <= mHistoryList.size() - 1; // index is not the last
    }

    private boolean canUndo() {
        return mEnableHistory // enable history
                && mSaveHistoryCapacity > 0 // capacity > 0
                && !mIsHistoryWorking.get() // history is not working
                && mHistoryList.size() > 0 // has history
                && mHistoryIndex > 0; // index is not the first
    }

    /**
     * 记录风格历史
     * @param text 文本
     */
    private void addTextToHistory(CharSequence text) {
        if (mHistoryList.size() >= mSaveHistoryCapacity) {
            mHistoryList.remove(0);
            mHistoryCursorIndexs.remove(0);
        }

        mHistoryList.add(mLastInput);
        mHistoryCursorIndexs.add(mLastCursorIndex);
        mHistoryIndex = mHistoryList.size();

        mLastInput = new SpannableStringBuilder(text);
        mLastCursorIndex = getSelectionEnd(); // 风格变化是选择一段文本, 所以以结尾为标准
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        if (mListener != null) {
            mListener.onCursorChange(selStart, selEnd, getSelectionStyles(selStart, selEnd));
        }
    }

}
