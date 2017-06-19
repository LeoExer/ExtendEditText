package com.leo.extendedittext;

/**
 * Created by Leo on 2017/3/17.
 */

public enum Rule {

    /**
     * 设置样式只对选中文本有影响
     */
    EXCLUSIVE_EXCLUSIVE(0),

    /**
     * 设置样式对选中的文本有影响, 并在其后输入的文本也会有该样式
     */
    EXCLUSIVE_INCLUSIVE(1),

    /**
     * 设置样式对选中的文本有影响, 并在其前输入的文本也会有该样式
     */
    INCLUSIVE_EXCLUSIVE(2),

    /**
     * 设置样式对选中的文本有影响, 并在其前后输入的文本都会有该样式
     */
    INCLUSIVE_INCLUSIVE(3);

    final int value;

    Rule(int value) {
        this.value = value;
    }
}
