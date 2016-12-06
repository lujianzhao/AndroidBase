package com.ljz.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ljz.base.common.utils.DensityUtil;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 作者: lujianzhao
 * 创建时间: 2016/05/10 11:36
 * 描述:
 */
public class TopBar extends FrameLayout {

    private TextView tv_right;
    private TextView tv_title;
    private TextView tv_left;

    private int leftTextColor;
    private Drawable leftBackground;
    private int leftTextSize;
    private String leftText;
    private boolean leftIsVisiable;
    private Drawable drawableLeftDrawableLeft;
    private Drawable drawableLeftDrawableRight;
    private float drawableLeftPadding;


    private int rightTextColor;
    private float drawableRightPadding;
    private Drawable rightBackground;
    private String rightText;
    private boolean rightIsVisiable;
    private Drawable drawableRightDrawable;

    private int titleTextSize;
    private int titleTextColor;
    private String toptitle;
//    private Drawable titleBackground;
    private int titleBackgroundColor;

    //定义三个布局参数
    private LayoutParams leftParams, rightParams, titleParams;

    //创建接口对象
    public OnTopbarClickListener listener;

    public String getLeftText() {
        return leftText;
    }

    //定义一个事件接口
    public interface OnTopbarClickListener {
        void leftClick();

        void rightClick();

        void titleClick();
    }

    //创建为事件接口赋值的方法
    public void setOnTopBarClickListener(OnTopbarClickListener listener) {
        this.listener = listener;
    }


    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将XML中定义的自定义属性映射到attrs中。
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);

        //从ta结构中获取数据，类似一种key,value结构，通过R.styleable.Topbar_属性名获取
        leftTextColor = ta.getColor(R.styleable.Topbar_topLeftTextColor, 0);
        leftBackground = ta.getDrawable(R.styleable.Topbar_topLeftBackground);
        drawableLeftDrawableLeft = ta.getDrawable(R.styleable.Topbar_topLeftDrawableLeft);
        drawableLeftDrawableRight = ta.getDrawable(R.styleable.Topbar_topLeftDrawableRight);
        leftText = ta.getString(R.styleable.Topbar_topLeftText);
        leftIsVisiable = ta.getBoolean(R.styleable.Topbar_topLeftIsVisiable, false);
        drawableLeftPadding = ta.getDimension(R.styleable.Topbar_topLeftDrawablePadding, 0);
        leftTextSize = ta.getDimensionPixelSize(R.styleable.Topbar_topLeftTextSize, 0);

        rightTextColor = ta.getColor(R.styleable.Topbar_topRightTextColor, 0);
        rightBackground = ta.getDrawable(R.styleable.Topbar_topRightBackground);
        rightText = ta.getString(R.styleable.Topbar_topRightText);
        rightIsVisiable = ta.getBoolean(R.styleable.Topbar_topRightIsVisiable, false);
        drawableRightPadding = ta.getDimension(R.styleable.Topbar_topRightDrawablePadding, 0);
        drawableRightDrawable = ta.getDrawable(R.styleable.Topbar_topRightDrawableLeft);

        titleTextSize = ta.getDimensionPixelSize(R.styleable.Topbar_topTitleTextSize, 0);
//        if (titleTextSize != 0) {
//            titleTextSize = AutoUtils.getPercentHeightSize((int) titleTextSize);
//        }


        titleTextColor = ta.getColor(R.styleable.Topbar_topTitleTextColor, 0);
        toptitle = ta.getString(R.styleable.Topbar_topTitle);
//        titleBackground = ta.getDrawable(R.styleable.Topbar_titleBackground);
        titleBackgroundColor = ta.getColor(R.styleable.Topbar_titleBackgroundColor, 0);

        //进行垃圾回收
        ta.recycle();

        LayoutInflater.from(context).inflate(R.layout.top_bar, this);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_title = (TextView) findViewById(R.id.tv_title);


        //设置控件的值
        tv_left.setTextColor(leftTextColor);          //设置文字颜色
        tv_left.setBackground(leftBackground);        //设置背景
        tv_left.setText(leftText);                    //设置文本
        if (leftTextSize != 0) {
            tv_left.setTextSize(AutoUtils.getPercentHeightSize(DensityUtil.px2sp(context, leftTextSize)));
        }
        tv_left.setGravity(Gravity.CENTER);
        if (drawableLeftDrawableLeft != null && drawableLeftDrawableRight != null) {
            drawableLeftDrawableLeft.setBounds(0, 0, drawableLeftDrawableLeft.getMinimumWidth(), drawableLeftDrawableLeft.getMinimumHeight());
            drawableLeftDrawableRight.setBounds(0, 0, drawableLeftDrawableRight.getMinimumWidth(), drawableLeftDrawableRight.getMinimumHeight());
            tv_left.setCompoundDrawables(drawableLeftDrawableLeft, null, drawableLeftDrawableRight, null);
            tv_left.setCompoundDrawablePadding((int) drawableLeftPadding);//设置图片和text之间的间距

        } else {
            if (drawableLeftDrawableLeft != null) {
                drawableLeftDrawableLeft.setBounds(0, 0, drawableLeftDrawableLeft.getMinimumWidth(), drawableLeftDrawableLeft.getMinimumHeight());
                tv_left.setCompoundDrawables(drawableLeftDrawableLeft, null, null, null);
                tv_left.setCompoundDrawablePadding((int) drawableLeftPadding);//设置图片和text之间的间距
            }

            if (drawableLeftDrawableRight != null) {
                drawableLeftDrawableRight.setBounds(0, 0, drawableLeftDrawableRight.getMinimumWidth(), drawableLeftDrawableRight.getMinimumHeight());
                tv_left.setCompoundDrawables(null, null, drawableLeftDrawableRight, null);
                tv_left.setCompoundDrawablePadding((int) drawableLeftPadding);//设置图片和text之间的间距
            }
        }

        tv_left.setPadding(DensityUtil.dip2px(context, 10), 0, DensityUtil.dip2px(context, 10), 0);

        tv_right.setTextColor(rightTextColor);          //设置文字颜色
        tv_right.setBackground(rightBackground);        //设置背景
        tv_right.setText(rightText);                    //设置文本
        tv_right.setGravity(Gravity.CENTER);
        if (drawableRightDrawable != null) {
            drawableRightDrawable.setBounds(0, 0, drawableRightDrawable.getMinimumWidth(), drawableRightDrawable.getMinimumHeight());
            tv_right.setCompoundDrawables(drawableRightDrawable, null, null, null);
            tv_right.setCompoundDrawablePadding((int) drawableRightPadding);//设置图片和text之间的间距
        }
        tv_right.setPadding(DensityUtil.dip2px(context, 10), 0, DensityUtil.dip2px(context, 10), 0);


        tv_title.setTextColor(titleTextColor);         //设置字体颜色
        if (titleTextSize != 0) {
            tv_title.setTextSize(AutoUtils.getPercentHeightSize(DensityUtil.px2sp(context, titleTextSize)));           //设置字体大小
        }
        tv_title.setText(toptitle);                    //设置文本
        tv_title.setGravity(Gravity.CENTER);           //居中显示
        setBackgroundColor(titleBackgroundColor);               //设置View的背景颜色


        //添加左侧按钮的Click事件
        tv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.leftClick();
                }

            }
        });

        //添加右侧按钮的Click事件
        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.rightClick();
                }
            }
        });

        tv_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.titleClick();
                }
            }
        });

        setLeftButtonIsVisiable(leftIsVisiable);
        setRightButtonIsVisiable(rightIsVisiable);

        AutoUtils.auto(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置左边按钮是否隐藏，true隐藏， false消失
     *
     * @param flag
     */
    public void setLeftButtonIsVisiable(boolean flag) {
        if (flag) {
            tv_left.setVisibility(View.VISIBLE);
        } else {
            tv_left.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边按钮是否隐藏，true隐藏， false消失
     *
     * @param flag
     */
    public void setRightButtonIsVisiable(boolean flag) {
        if (flag) {
            tv_right.setVisibility(View.VISIBLE);
        } else {
            tv_right.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题中间的文字
     * @param toptitle
     */
    public void setToptitle(String toptitle) {
        this.toptitle = toptitle;
        if (tv_title != null && !TextUtils.isEmpty(toptitle)) {
            tv_title.setText(toptitle);
        }
    }

    /**
     * 设置左边的文字
     * @param leftText
     */
    public void setLeftText(String leftText) {
        this.leftText = leftText;
        if (tv_left != null && !TextUtils.isEmpty(leftText)) {
            tv_left.setText(leftText);
        }
    }
}
