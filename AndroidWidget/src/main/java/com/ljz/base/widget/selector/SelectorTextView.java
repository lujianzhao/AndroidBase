package com.ljz.base.widget.selector;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

import com.ljz.base.widget.selector.helper.AppCompatTextViewHelper;
import com.ljz.base.widget.selector.injection.SelectorInjection;

/**
 * <p>
 * \<SelectorTextView
 * android:id="@+id/stv"
 * android:text="Click Me"
 * app:normalColor="#03a9f4"                     //必填
 * app:normalDrawable="@drawable/btn_oval_shape" //必填
 * app:normalStrokeColor="#ffffff"
 * app:normalStrokeWidth="4dp"
 * app:pressedColor="#03f4e8"
 * /\>
 * </p>
 */
public class SelectorTextView extends CheckedTextView implements SelectorView {

    private SelectorInjection injection;

    public SelectorTextView(Context context) {
        this(context, null);
    }

    public SelectorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        injection = initSelectorInjection(context, attrs);
        injection.injection(this);

        new AppCompatTextViewHelper(this).loadFromAttributes(attrs, defStyle);

        setClickable(true);
//        setGravity(Gravity.CENTER);
    }

    @Override
    public SelectorInjection initSelectorInjection(Context context, AttributeSet attr) {
        return new SelectorInjection(context, attr);
    }

    @Override
    public SelectorInjection getInjection() {
        return injection;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        injection.setEnabled(this, enabled);
    }
}