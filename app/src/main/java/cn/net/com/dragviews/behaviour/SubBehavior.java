package cn.net.com.dragviews.behaviour;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.net.com.dragviews.MyFrameLayout;


/**
 * Created by Administrator on 2017/2/13.
 */

public class SubBehavior<V extends View> extends CoordinatorLayout.Behavior<View> {

    String TAG = "SubBehavior";

    public SubBehavior() {
        super();
    }

    public SubBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child,
                                   View dependency) {
        boolean ret = dependency instanceof MyFrameLayout;

        return ret;
    }


    int mMaxOffset = 80;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child,
                                          View dependency) {
        // Log.d("tag", "child状态改变");
        int left = dependency.getLeft();
        if (left < MyFrameLayout.mLeftWidth) {
            int childLeft = child.getLeft();
            int tmp = left * mMaxOffset / MyFrameLayout.mLeftWidth;
            int offset = tmp - mMaxOffset;// - child.getLeft();//dependency.getLeft() - child.getLeft();
            //  ViewCompat.offsetLeftAndRight(child, offset);
            child.layout(offset, 0, child.getWidth(), child.getHeight());
        } else if (left == MyFrameLayout.mLeftWidth) {
            //  ViewCompat.offsetLeftAndRight(child, 80);
            child.layout(0, 0, child.getWidth(), child.getHeight());
        }
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);

        // return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        Log.d(TAG, "dxConsumed:" + String.valueOf(dxConsumed) + ",dyConsumed:" + String.valueOf(dyConsumed)
                + ",dxUnconsumed:" + String.valueOf(dxUnconsumed) + ",dyUnconsumed:" + String.valueOf(dyUnconsumed));
        int x = 0;
        x++;
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        // return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);


        //  child.Me

        int specModeWidth = View.MeasureSpec.getMode(parentWidthMeasureSpec);
        int specSizeWidth = View.MeasureSpec.getSize(parentWidthMeasureSpec);
        if (specModeWidth == View.MeasureSpec.EXACTLY) {
            int x = 0;
            x++;
        } else if (specModeWidth == View.MeasureSpec.AT_MOST) {
            int x = 0;
            x++;
        } else if (specModeWidth == View.MeasureSpec.UNSPECIFIED) {
            int x = 0;
            x++;
        }

        int specModeHeight = View.MeasureSpec.getMode(parentHeightMeasureSpec);
        int specSizeHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec);
        if (specModeHeight == View.MeasureSpec.EXACTLY) {
            int x = 0;
            x++;
        } else if (specModeHeight == View.MeasureSpec.AT_MOST) {
            int x = 0;
            x++;
        } else if (specModeHeight == View.MeasureSpec.UNSPECIFIED) {
            int x = 0;
            x++;
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(specSizeWidth, View.MeasureSpec.EXACTLY);
        //  int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(300,specModeHeight);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(specSizeHeight, View.MeasureSpec.EXACTLY);

        child.measure(widthMeasureSpec, heightMeasureSpec);
        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        // return super.onLayoutChild(parent, child, layoutDirection);

        int width = child.getMeasuredWidth();
        int height = child.getMeasuredHeight();
        int left = mMaxOffset;
        int top = 0;
        child.layout(-left, top, width, height);

        return true;
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        MyFrameLayout childViewdependency = (MyFrameLayout) parent.getChildAt(1);

        boolean ret = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ret = true;

                childViewdependency.action_Down(ev);
                Log.d(TAG, "MotionEvent.ACTION_DOWN ");
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                Log.d(TAG, "MotionEvent.ACTION_MOVE ");
                childViewdependency.action_Move(ev);
                int x = 0;
                x++;
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                Log.d(TAG, "MotionEvent.ACTION_UP ");
                childViewdependency.action_Up(ev);
            }
            break;
        }


        return ret ? true : super.onTouchEvent(parent, child, ev);
    }
}
