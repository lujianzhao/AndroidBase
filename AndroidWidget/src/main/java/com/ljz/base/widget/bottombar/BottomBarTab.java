package com.ljz.base.widget.bottombar;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by YoKeyword on 16/6/3.
 */
public class BottomBarTab extends FrameLayout {

    private int mTabPosition = -1;

    private TextView mMesTextView;

    public BottomBarTab(Context context, @DrawableRes int icon, String title, int textColor) {
        this(context, null, icon, title, textColor);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int icon, String title, int textColor) {
        this(context, attrs, 0, icon, title, textColor);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int defStyleAttr, int icon, String title, int textColor) {
        super(context, attrs, defStyleAttr);
        init(context, icon, title, textColor);
    }

    private void init(Context context, int icon, String title, int textColor) {
        LayoutInflater.from(context).inflate(com.ljz.base.widget.R.layout.tab_indicator, this);
        TextView  textView = (TextView) findViewById(com.ljz.base.widget.R.id.tab_title);
        ImageView imageView = (ImageView) findViewById(com.ljz.base.widget.R.id.tab_icon);
        mMesTextView = (TextView) findViewById(com.ljz.base.widget.R.id.tab_mes);

        textView.setText(title);
        textView.setTextColor(context.getResources().getColorStateList(textColor));

        imageView.setImageResource(icon);
    }


    public void setTabPosition(int position) {
        mTabPosition = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    public int getTabPosition() {
        return mTabPosition;
    }


    public void showTabMes(int num) {
        mMesTextView.setVisibility(VISIBLE);
        mMesTextView.setText(String.valueOf(num));
    }

    public void hintTabMes() {
        mMesTextView.setVisibility(INVISIBLE);
    }
}
