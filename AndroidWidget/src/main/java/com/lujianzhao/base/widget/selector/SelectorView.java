package com.lujianzhao.base.widget.selector;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.lujianzhao.base.widget.selector.injection.SelectorInjection;

/**
 * @author Kale
 * @date 2016/3/14
 */
public interface SelectorView extends Checkable {

    int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    SelectorInjection initSelectorInjection(Context context, AttributeSet attr);

    SelectorInjection getInjection();
}
