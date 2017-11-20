package com.tongtong.slideclose.slide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.tongtong.slideclose.MyApp;
import com.tongtong.slideclose.R;

/**
 * 侧滑关闭布局
 * Created by allen on 2017/11/18.
 */

public class SlidingLayout extends FrameLayout {
    // 页面边缘阴影的宽度默认值
    private static final int SHADOW_WIDTH = 10;
    //绑定的Activity
    private Activity mActivity;
    //滚动器
    private Scroller mScroller;
    // 页面边缘的阴影图
    private Drawable mLeftShadow;
    // 页面边缘阴影的宽度
    private int mShadowWidth;
    private int mInterceptDownX;
    private int mLastInterceptX;
    private int mLastInterceptY;
    private int mTouchDownX;
    private int mLastTouchX;
    private int mLastTouchY;
    private boolean isConsumed = false;
    private Activity mBeforeActivity;
    private int offX;

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mScroller = new Scroller(context);
        mLeftShadow = getResources().getDrawable(R.drawable.left_shadow);
        int density = (int) getResources().getDisplayMetrics().density;
        mShadowWidth = SHADOW_WIDTH * density;
        if (MyApp.mActivities.size() > 1) {
            mBeforeActivity = MyApp.mActivities.get(MyApp.mActivities.size() - 2);
            if (mBeforeActivity != null) {
                offX = -mBeforeActivity.getWindowManager().getDefaultDisplay().getWidth() / 4;
                mBeforeActivity.getWindow().getDecorView().setTranslationX(offX);
            }
        }
    }

    /**
     * 绑定Activity
     */
    public void bindActivity(Activity activity) {
        mActivity = activity;
        ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        View child = decorView.getChildAt(0);
        decorView.removeView(child);
        addView(child);
        decorView.addView(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                mInterceptDownX = x;
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastInterceptX;
                int deltaY = y - mLastInterceptY;
                // 手指处于屏幕边缘，且横向滑动距离大于纵向滑动距离时，拦截事件
                if (mInterceptDownX < (getWidth() / 10) && Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                mInterceptDownX = mLastInterceptX = mLastInterceptY = 0;
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = x;
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastTouchX;
                int deltaY = y - mLastTouchY;
                // 手指处于屏幕边缘，且横向滑动距离大于纵向滑动距离时，表示需要侧滑关闭
                if (!isConsumed && mTouchDownX < (getWidth() / 10) && Math.abs(deltaX) > Math.abs(deltaY)) {
                    isConsumed = true;
                }

                if (isConsumed) {
                    int rightMovedX = mLastTouchX - (int) ev.getX();
                    // 左侧即将滑出屏幕
                    if (getScrollX() + rightMovedX >= 0) {
                        scrollTo(0, 0);
                    } else {
                        scrollBy(rightMovedX, 0);
                        if (mBeforeActivity != null) {
                            offX = rightMovedX < 0 ? offX + Math.abs(rightMovedX) / 4 : offX - Math.abs(rightMovedX) / 4;
                            mBeforeActivity.getWindow().getDecorView().setTranslationX(offX);
                        }
                    }
                }
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            case MotionEvent.ACTION_UP:
                isConsumed = false;
                mTouchDownX = mLastTouchX = mLastTouchY = 0;
                // 根据手指释放时的位置决定回弹还是关闭
                if (-getScrollX() < getWidth() / 2) {
                    scrollBack();
                } else {
                    scrollClose();
                }
                break;
        }
        return true;
    }

    /**
     * 滑动返回
     */
    private void scrollBack() {
        int startX = getScrollX();
        int dx = -getScrollX();
        mScroller.startScroll(startX, 0, dx, 0, 300);
        invalidate();
        offX = -getWidth() / 4;
    }

    /**
     * 滑动关闭
     */
    private void scrollClose() {
        if (mBeforeActivity != null) {
            mBeforeActivity.getWindow().getDecorView().setTranslationX(0);
        }
        int startX = getScrollX();
        int dx = -getScrollX() - getWidth();
        mScroller.startScroll(startX, 0, dx, 0, 300);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        } else if (-getScrollX() >= getWidth()) {
            mActivity.finish();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawShadow(canvas);
    }

    /**
     * 绘制边缘的阴影
     */
    private void drawShadow(Canvas canvas) {
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());
        canvas.save();
        canvas.translate(-mShadowWidth, 0);
        mLeftShadow.draw(canvas);
        canvas.restore();
    }
}
