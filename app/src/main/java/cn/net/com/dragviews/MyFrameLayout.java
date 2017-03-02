package cn.net.com.dragviews;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MyFrameLayout extends FrameLayout {
    Scroller mScroller;

    public MyFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }

    float preXpos = -1;
    VelocityTracker vTracker = VelocityTracker.obtain();

    public static int mLeftWidth = 380;

    public final static int DOCK_LEFT = 1;
    public final static int DOCK_MID = 2;
    public final static int DOCK_RIGHT = 3;

    boolean mIsLeft = true;
    int mDockType = DOCK_LEFT;

    OnSlideListen mOnSlideListen = null;

    public void setOnSlideListen(OnSlideListen onSlideListen){
        mOnSlideListen = onSlideListen;
    }

    public void action_Down(MotionEvent event){
        preXpos = event.getX();

//        if (vTracker == null) {
//            vTracker = VelocityTracker.obtain();
//        } else {
//            vTracker.clear();
//        }
//        vTracker.addMovement(event);
    }

    public void action_Move(MotionEvent event){
       // vTracker.addMovement(event);

        float curXpos = event.getX();
        float distance = curXpos - preXpos;

        View view = (View) getParent();
        //   int scrollX = view.getScrollX();

        int left = getLeft();
        // offsetTopAndBottom((int) distance);

        Log.d("computeScroll", "ACTION_MOVE preXpos:" + String.valueOf(preXpos) + ",curXpos:" + String.valueOf(curXpos)
                + ",left:" + String.valueOf(left));


        int width = view.getWidth();
        //  int selfWidth = getWidth();

        if (mDockType == DOCK_LEFT) {
            if (left >= 0 && left <= mLeftWidth) {
                // ((View) getParent()).scrollBy(-(int) distance, 0);
                offsetLeftAndRight((int) distance);
            }
        } else if (mDockType == DOCK_MID) {
            if (left >= 0 && left <= width) {
                // ((View) getParent()).scrollBy(-(int) distance, 0);
                offsetLeftAndRight((int) distance);
            }
        } else if (mDockType == DOCK_RIGHT) {
            if (left >= 0 && left >= mLeftWidth && left <= width) {
                //  ((View) getParent()).scrollBy(-(int) distance, 0);

                //修复边界
                if (distance + left < mLeftWidth){
                    distance = mLeftWidth - left;
                }else if (distance + left > width){
                    distance = width - left;
                }
                offsetLeftAndRight((int) distance);
            }
        }

        preXpos = curXpos;
    }

    public void action_Up(MotionEvent event){


        View view = (View) getParent();
        int scrollX = view.getScrollX();

        int left = getLeft();


        int width = view.getWidth();
        //     int right = getWidth();
        //  int left = getLeft();


        int distance = 0;



        Log.d("computeScroll", "ACTION_UP left:" + String.valueOf(left) /*+ ",scrollY:" + String.valueOf(scrollY)*/
                        /*+ ",XVelocity:" + String.valueOf(XVelocity) */+ ",width:" + String.valueOf(width)
                        /*+ ",left:" + String.valueOf(left) + ",right:" + String.valueOf(right)*/);

        switch (mDockType) {
            case DOCK_LEFT: {
                if (left > (mLeftWidth) / 2) {//往右滚动
                    distance = (mLeftWidth - left);
                    mDockType = DOCK_MID;
                } else {//往左滚动
                    distance = -left;
                    mDockType = DOCK_LEFT;
                }
                mScroller.startScroll(0, 0, distance, 0);
                invalidate();
            }
            break;
            case DOCK_MID: {
                if (left > mLeftWidth) {//右半边
                    if (left - mLeftWidth > (width - mLeftWidth) / 2) {//往右滚动
                        distance = (width - left);
                        mDockType = DOCK_RIGHT;
                    } else {//往左滚动
                        distance = mLeftWidth - left;
                        mDockType = DOCK_MID;
                    }

                    mScroller.startScroll(0, 0, distance, 0);
                    invalidate();
                } else if (left < mLeftWidth) {//左半边
                    if (left > (mLeftWidth) / 2) {//往右滚动
                        distance = (mLeftWidth - left);
                        mDockType = DOCK_MID;
                    } else {//往左滚动
                        distance = -left;
                        mDockType = DOCK_LEFT;
                    }
                    mScroller.startScroll(0, 0, distance, 0);
                    invalidate();
                } else {
                    mDockType = DOCK_MID;
                }
            }
            break;
            case DOCK_RIGHT: {
                if (left-mLeftWidth > (width - mLeftWidth) / 2) {//往右滚动
                    distance = (width - left);
                    mDockType = DOCK_RIGHT;
                } else {//往左滚动
                    distance = mLeftWidth - left;
                    mDockType = DOCK_MID;
                }

                mScroller.startScroll(0, 0, distance, 0);
                invalidate();
            }
            break;
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                preXpos = event.getX();

                if (vTracker == null) {
                    vTracker = VelocityTracker.obtain();
                } else {
                    vTracker.clear();
                }
                vTracker.addMovement(event);

                Log.d("computeScroll", "ACTION_DOWN preYpos:" + String.valueOf(preXpos));
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                vTracker.addMovement(event);

                float curXpos = event.getX();
                float distance = curXpos - preXpos;

                View view = (View) getParent();
                //   int scrollX = view.getScrollX();

                int left = getLeft();
                // offsetTopAndBottom((int) distance);

                Log.d("computeScroll", "ACTION_MOVE preXpos:" + String.valueOf(preXpos) + ",curXpos:" + String.valueOf(curXpos)
                        + ",left:" + String.valueOf(left));


                int width = view.getWidth();
                //  int selfWidth = getWidth();

                if (mDockType == DOCK_LEFT) {
                    if (left >= 0 && left <= mLeftWidth) {
                        // ((View) getParent()).scrollBy(-(int) distance, 0);
                        //修复边界
                        if (distance + left < 0){
                            distance = -left;
                        }else if (distance + left > mLeftWidth){
                            distance = mLeftWidth - left;
                        }

                        offsetLeftAndRight((int) distance);
                    }
                } else if (mDockType == DOCK_MID) {
//                    if (left >= 0 && left <= width) {
//                        // ((View) getParent()).scrollBy(-(int) distance, 0);
//                        offsetLeftAndRight((int) distance);
//                    }

                    if (left >= 0 && left <= width) {
                        //修复边界
                        if (distance + left < 0){
                            distance = -left;
                        }else if (distance + left > width){
                            distance = width - left;
                        }
                        offsetLeftAndRight((int) distance);
                    }

                } else if (mDockType == DOCK_RIGHT) {
                    if (left >= 0 && left >= mLeftWidth && left <= width) {
                        //  ((View) getParent()).scrollBy(-(int) distance, 0);

                        //修复边界
                        if (distance + left < mLeftWidth){
                            distance = mLeftWidth - left;
                        }else if (distance + left > width){
                            distance = width - left;
                        }
                        offsetLeftAndRight((int) distance);
                    }
                }
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                vTracker.addMovement(event);

                vTracker.computeCurrentVelocity(1000);

                float XVelocity = vTracker.getXVelocity();

                View view = (View) getParent();
                int scrollX = view.getScrollX();

                int left = getLeft();


                int width = view.getWidth();
                //     int right = getWidth();
                //  int left = getLeft();

                boolean hasVelocity = false;
                int distance = 0;
                if (Math.abs(XVelocity) >= 100) {
                    hasVelocity = true;
                }


                Log.d("computeScroll", "ACTION_UP left:" + String.valueOf(left) /*+ ",scrollY:" + String.valueOf(scrollY)*/
                                + ",XVelocity:" + String.valueOf(XVelocity) + ",width:" + String.valueOf(width)
                        /*+ ",left:" + String.valueOf(left) + ",right:" + String.valueOf(right)*/);

                switch (mDockType) {
                    case DOCK_LEFT: {
                        if (left > (mLeftWidth) / 2) {//往右滚动
                            distance = (mLeftWidth - left);
                            mDockType = DOCK_MID;
                        } else {//往左滚动
                            distance = -left;
                            mDockType = DOCK_LEFT;
                        }
                        mScroller.startScroll(0, 0, distance, 0);
                        invalidate();
                    }
                    break;
                    case DOCK_MID: {
                        if (left > mLeftWidth) {//右半边
                            if (left - mLeftWidth > (width - mLeftWidth) / 2) {//往右滚动
                                distance = (width - left);
                                mDockType = DOCK_RIGHT;
                            } else {//往左滚动
                                distance = mLeftWidth - left;
                                mDockType = DOCK_MID;
                            }

                            mScroller.startScroll(0, 0, distance, 0);
                            invalidate();
                        } else if (left < mLeftWidth) {//左半边
                            if (left > (mLeftWidth) / 2) {//往右滚动
                                distance = (mLeftWidth - left);
                                mDockType = DOCK_MID;
                            } else {//往左滚动
                                distance = -left;
                                mDockType = DOCK_LEFT;
                            }
                            mScroller.startScroll(0, 0, distance, 0);
                            invalidate();
                        } else {
                            mDockType = DOCK_MID;
                        }
                    }
                    break;
                    case DOCK_RIGHT: {
                        if (left-mLeftWidth > (width - mLeftWidth) / 2) {//往右滚动
                            distance = (width - left);
                            mDockType = DOCK_RIGHT;
                        } else {//往左滚动
                            distance = mLeftWidth - left;
                            mDockType = DOCK_MID;
                        }

                        mScroller.startScroll(0, 0, distance, 0);
                        invalidate();
                    }
                    break;
                }

                vTracker.recycle();
                vTracker = null;

            }
            break;
        }
        return true;
    }

    int curOffset = -1;

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            int startX = mScroller.getStartX();
            int left = getLeft();
            int distance = currX + left;
            if (curOffset == -1) {
                curOffset = currX;
            }
            distance = currX - curOffset;
            curOffset = currX;
            Log.d("computeScroll", "currX:" + String.valueOf(currX) + ",startX:" + String.valueOf(startX)
                    + ",distance:" + String.valueOf(distance) + ",left:" + String.valueOf(left));

            // offsetTopAndBottom( distance);
            View view = (View) getParent();
            //  view.scrollTo(currX, 0);

            offsetLeftAndRight(distance);
            invalidate();
        } else {
            curOffset = -1;
            int left = getLeft();

            switch (mDockType) {
                case DOCK_LEFT: {
                    if (left > 0) {
                        offsetLeftAndRight(-left);
                    }
                    mHandler.obtainMessage().sendToTarget();


                }
                break;
                case DOCK_MID: {
                    if (left != mLeftWidth){
                        offsetLeftAndRight(mLeftWidth-left);
                    }
                    mHandler.obtainMessage().sendToTarget();
                }
                break;
                case DOCK_RIGHT: {
                    View view = (View) getParent();
                    int pwidth = view.getWidth();
                    if (left != pwidth){
                        offsetLeftAndRight(pwidth-left);
                    }
                    mHandler.obtainMessage().sendToTarget();
                }
                break;
            }
        }
    }

    public static interface OnSlideListen{
        public void onSlideFinish(int nDockType); //返回
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);

            if (null != mOnSlideListen){
                mOnSlideListen.onSlideFinish(mDockType);
            }
        }
    };
}
