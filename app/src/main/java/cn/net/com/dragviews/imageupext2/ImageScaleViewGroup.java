package cn.net.com.dragviews.imageupext2;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ListView;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/2/23.
 */

public class ImageScaleViewGroup extends ViewGroup {
    static String TAG = "ImageScaleViewGroup";

    int mTouchSlop = 0;
    int mMinFlingVelocity = 0;
    int mMaxFlingVelocity = 0;
    ScrollerCompat mScroller;
    int mPreCurY = 0;

    int mSrcHeightHead = -1;
    boolean mScaleBig = false;  //fling情况下 能不能拉伸
    int mStationHeightUp = 0;   //上面停留在可见区域的高度

    boolean mScaled = true;  //上面的view能不能拉伸

    public ImageScaleViewGroup(Context context) {
        super(context);
        init(context);
    }

    public ImageScaleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageScaleViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageScaleViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();

        mScroller = ScrollerCompat.create(context, sQuinticInterpolator);
    }

    static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public void setSrcHeightHead(int srcHeightHead) {//可能不能由外部设置
        // mSrcHeightHead = srcHeightHead;
    }

    public void setStationHeightUp(int stationHeightUp) {
        mStationHeightUp = stationHeightUp;
    }

    public void setScaled(boolean scaled) {
        mScaled = scaled;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        int widthModeParent = View.MeasureSpec.getMode(widthMeasureSpec);
//        int widthSizeParent = View.MeasureSpec.getSize(widthMeasureSpec);
//
//        int heightModeParent = View.MeasureSpec.getMode(heightMeasureSpec);
//        int heightSizeParent = View.MeasureSpec.getSize(heightMeasureSpec);
//
//        View child0View = getChildAt(0);
//        int child0RealWidth = 0;
//        int child0RealHeight = 0;
//
//        ViewGroup.LayoutParams layoutParams0 = child0View.getLayoutParams();
//        int child0Width = layoutParams0.width;
//        int child0Height = layoutParams0.height;
//        if (child0Width > 0) {
//            child0RealWidth = child0Width;
//        } else {
//            child0RealWidth = widthSizeParent;
//        }
//        if (child0Height > 0) {
//            child0RealHeight = child0Height;
//        } else {
//            child0RealHeight = heightSizeParent;
//        }
//        child0View.measure(View.MeasureSpec.makeMeasureSpec(child0RealWidth, MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(child0RealHeight, MeasureSpec.EXACTLY));
//
//
//        View child1View = getChildAt(1);
//        int child1RealWidth = 0;
//        int child1RealHeight = 0;
//
//        ViewGroup.LayoutParams layoutParams1 = child1View.getLayoutParams();
//        int child1Width = layoutParams1.width;
//        int child1Height = layoutParams1.height;
//        if (child1Width > 0) {
//            child1RealWidth = child1Width;
//        } else {
//            child1RealWidth = widthSizeParent;
//        }
//        if (child1Height > 0) {
//            child1RealHeight = child1Height;
//        } else {
//            child1RealHeight = heightSizeParent;
//        }
//        if (child1RealHeight > mStationHeightUp) {
//            child1RealHeight -= mStationHeightUp;
//        }
//
//        int child1ItemsTotalHeight = 0;
//        ViewGroup viewGroup1 = (ViewGroup) child1View;
//        int child1Count = viewGroup1.getChildCount();
//        for (int i = 0; i < child1Count; i++) {
//            View child1child = viewGroup1.getChildAt(i);
//            if (null != child1child) {
//                int nMeasuredHeight = child1child.getMeasuredHeight();
//                child1ItemsTotalHeight += nMeasuredHeight;
//
//                if (child1ItemsTotalHeight >= child1RealHeight) {
//                    break;
//                }
//            }
//        }
//
//        if (mSrcHeightHead == -1) {
//            mSrcHeightHead = child0RealHeight;
//        }
//
//        if ((child1ItemsTotalHeight > 0) && mSrcHeightHead > 0 && child1ItemsTotalHeight < heightSizeParent - mSrcHeightHead) {
//            child1ItemsTotalHeight = heightSizeParent - mSrcHeightHead;
//        }
//
//        if ((child1ItemsTotalHeight > 0) && child1ItemsTotalHeight < child1RealHeight) {
//            child1RealHeight = child1ItemsTotalHeight;
//        }
//
//        child1View.measure(View.MeasureSpec.makeMeasureSpec(child1RealWidth, MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(child1RealHeight, MeasureSpec.EXACTLY));
//
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthModeParent = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSizeParent = View.MeasureSpec.getSize(widthMeasureSpec);

        int heightModeParent = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSizeParent = View.MeasureSpec.getSize(heightMeasureSpec);

        View child0View = getChildAt(0);
        int child0RealWidth = 0;
        int child0RealHeight = 0;

        ViewGroup.LayoutParams layoutParams0 = child0View.getLayoutParams();
        int child0Width = layoutParams0.width;
        int child0Height = layoutParams0.height;
        if (child0Width > 0) {
            child0RealWidth = child0Width;
        } else {
            child0RealWidth = widthSizeParent;
        }
        if (child0Height > 0) {
            child0RealHeight = child0Height;
        } else {
            child0RealHeight = heightSizeParent;
        }
        child0View.measure(View.MeasureSpec.makeMeasureSpec(child0RealWidth, MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(child0RealHeight, MeasureSpec.EXACTLY));


        View child1View = getChildAt(1);
        int child1RealWidth = 0;
        int child1RealHeight = 0;

        ViewGroup.LayoutParams layoutParams1 = child1View.getLayoutParams();
        int child1Width = layoutParams1.width;
        int child1Height = layoutParams1.height;
        if (child1Width > 0) {
            child1RealWidth = child1Width;
        } else {
            child1RealWidth = widthSizeParent;
        }
        if (child1Height > 0) {
            child1RealHeight = child1Height;
        } else {
            child1RealHeight = heightSizeParent;
        }
        if (child1RealHeight > mStationHeightUp) {
            child1RealHeight -= mStationHeightUp;
        }

        child1View.measure(View.MeasureSpec.makeMeasureSpec(child1RealWidth, MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(child1RealHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child0View = getChildAt(0);

        int leftChild0 = child0View.getLeft();
        int topChild0 = child0View.getTop();
        int widthChild0 = child0View.getMeasuredWidth();
        int heightChild0 = child0View.getMeasuredHeight();
        child0View.layout(leftChild0, topChild0, leftChild0 + widthChild0, topChild0 + heightChild0);


        View child1View = getChildAt(1);

        int leftChild1 = child1View.getLeft();
        int topChild1 = child1View.getTop();
        int widthChild1 = child1View.getMeasuredWidth();
        int heightChild1 = child1View.getMeasuredHeight();
        int topChild1Real = topChild0 + heightChild0;
        child1View.layout(leftChild1, topChild1Real, leftChild1 + widthChild1, topChild1Real + heightChild1);
    }

    float mPreY = 0f;
    private boolean mSliding = false;  //是否正在滑动
    VelocityTracker vTracker = VelocityTracker.obtain();

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean bret = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mPreY = ev.getY();
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                float curY = ev.getY();
                float distance = curY - mPreY;
                int nTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if (Math.abs(distance) >= nTouchSlop) {
                    mSliding = bret = true;

                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }
                } else {
                    mSliding = bret = false;
                }

            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                bret = mSliding;
            }
            break;
        }
        return bret ? true : super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean bret = false;

        vTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mPreY = event.getY();
                bret = true;
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                float curY = event.getY();
                float distance = curY - mPreY;
                if (distance != 0) {
                    bret = true;

                    if (mSrcHeightHead == -1) {
                        View child0View = getChildAt(0);
                        mSrcHeightHead = child0View.getHeight();
                    }

                    mScaleBig = mScaled;

                    scrollBy(distance);
                }

                mPreY = curY;
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                //vTracker.computeCurrentVelocity(1000,mMaxFlingVelocity);
                vTracker.computeCurrentVelocity(1000);

                float yvel = vTracker.getYVelocity();
                if (yvel != 0) {
                    mPreCurY = 0;

                    View child0View = getChildAt(0);
                    if (-1 != mSrcHeightHead && child0View.getTop() == 0 && child0View.getHeight() > mSrcHeightHead) {
                        int dy = mSrcHeightHead - child0View.getHeight();
                        mScroller.startScroll(0, 0, 0, dy);
                    } else {
                        mScaleBig = false;
                        mScroller.fling(0, 0, 0, (int) yvel, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    }

                    invalidate();
                }


                vTracker.clear();

                bret = true;
            }
            break;
        }
        return bret ? true : super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        // super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currY = mScroller.getCurrY();
            int finalY = mScroller.getFinalY();

            float curVelocity = mScroller.getCurrVelocity();

            Log.d(TAG, "computeScroll() currY:" + String.valueOf(currY) + " finalY:" + String.valueOf(finalY) + " curVelocity:" + String.valueOf(curVelocity));

            float distance = currY - mPreCurY;
            if (distance != 0) {
                scrollBy(distance);
            }

            mPreCurY = currY;


            View child0View = getChildAt(0);
            View child1View = getChildAt(1);

            int bottomChild1 = child1View.getBottom();
            int topChild0 = child0View.getTop();
            boolean bConsumFull = ((distance < 0 && getHeight() == bottomChild1 && !child1View.canScrollVertically(1))
                    || (distance > 0 && topChild0 == 0 && !child1View.canScrollVertically(-1)));


            if (mScroller.isFinished() || bConsumFull) {
                mScroller.abortAnimation();
            } else {
                invalidate();
            }
        }
    }

    private void scrollBy(float distance) {
        View child0View = getChildAt(0);
        View child1View = getChildAt(1);
        float distanceRemain = 0;

        int child0Top = child0View.getTop();
        int child0Height = child0View.getHeight();
        if (distance < 0) {//向上
            int child1Top = child1View.getTop();
            int child1Height = child1View.getHeight();

            //child0View 缩小
            if (-1 != mSrcHeightHead && child0View.getHeight() > mSrcHeightHead) {
                float off = distance;
                // distance = 0;
                if (child0View.getHeight() + distance < mSrcHeightHead) {
                    // distance = mSrcHeightHead
                    off = -(child0View.getHeight() - mSrcHeightHead);
                    distance -= off;
                } else {
                    distance = 0;
                }

                child0View.getLayoutParams().height += off;
                child1Top += off;  //child0view 缩小的同时, child1view 的高度也会随之上升 这里很重要
                requestLayout();
            }

            if (distance != 0) {
                if (child1View.canScrollVertically(1)) {
                    if (child0Top + child0Height + distance - mStationHeightUp <= 0) {
                        distanceRemain = -(distance + (child0Top + child0Height - mStationHeightUp));//正数
                        distance = -(child0Top + child0Height - mStationHeightUp);//负数
                    }
                } else {
                    int child1Count = 0;
                    if (child1View instanceof ViewGroup) {
                        child1Count = ((ViewGroup) child1View).getChildCount();
                    }
                    if (child1Count > 0) {
                        ViewGroup viewGroupChild1 = (ViewGroup) child1View;

                        View child1LastItem = viewGroupChild1.getChildAt(child1Count - 1);

                        int child1ViewBottom = viewGroupChild1.getBottom();
                        int child1LastItemBottom = child1LastItem.getBottom() + child1Top;  //相对于 ImageScaleViewGroup 的位置
                        //增加 child1ViewBottom > getHeight()  来控制 ScrollView
                        if (child1LastItemBottom > getHeight() && child1ViewBottom > getHeight()) {
                            if (child1LastItemBottom + distance <= getHeight()) {
                                distance = -(child1LastItemBottom - getHeight());
                            }
                        } else {
                            distance = 0;
                            distanceRemain = 0;
                        }
                    } else {
                        int child1ViewBottom = child1View.getBottom();
                        if (child1ViewBottom > getHeight()){//支持单个view
                            if (child1ViewBottom + distance <= getHeight()) {
                                distance = -(child1ViewBottom - getHeight());
                            }
                        }else {
                            distance = 0;
                            distanceRemain = 0;
                        }
                    }
                }
            }
        } else { //向下
            //  if (child1View instanceof RecyclerView) {
            int nScrollOffset = 0;
            try {
                Class c = Class.forName(child1View.getClass().getName());
                Method computeVerticalScrollOffset = findcomputeVerticalScrollOffsetMethod(c);//c.getDeclaredMethod("computeVerticalScrollOffset");
                computeVerticalScrollOffset.setAccessible(true);
                nScrollOffset = (int) computeVerticalScrollOffset.invoke(child1View);
            } catch (Exception ex) {

            }
            //child1View.computeVerticalScrollOffset();

            // RecyclerView recyclerView = (RecyclerView) child1View;
            // int nScrollOffset = recyclerView.computeVerticalScrollOffset();
            //   int nScrollExtent = recyclerView.computeVerticalScrollExtent();
            //   int nScrollRange = recyclerView.computeVerticalScrollRange();
            //   int child1Height = child1View.getHeight();

            if (nScrollOffset > 0) {//内部有滚动,那么就要计算内部滚动距离,其他分配给整体滚动
                if (nScrollOffset - distance <= 0) {
                    distanceRemain = -nScrollOffset;
                    distance = distance - nScrollOffset;
                } else {
                    distanceRemain = -distance;//负数
                    distance = 0;
                }
            } else {
                if (child0Top + distance >= 0) {
                    int off = (int) (child0Top + distance);
                    distance = -child0Top;

                    //child0View放大
                    if (mScaleBig) {//fling不能引起拉伸
                        child0View.getLayoutParams().height += off;
                        requestLayout();
                    }
                }
            }
            //           }
        }

        if (0 != (int) distance) {
            child0View.offsetTopAndBottom((int) distance);
            child1View.offsetTopAndBottom((int) distance);
        }
        if (0 != (int) distanceRemain) {
            if (child1View instanceof ListView) {
                ((ListView) child1View).smoothScrollBy((int) distanceRemain, 0);
            }/*else if (child1View instanceof ScrollView){
                ((ScrollView) child1View).smoothScrollBy(0,(int) distanceRemain);
                //child1View.scrollBy(0, (int) distanceRemain);
            }*/ else {
                child1View.scrollBy(0, (int) distanceRemain);//外部无法滚动的时候,内部滚动
            }
        }
    }

    private Method findcomputeVerticalScrollOffsetMethod(Class c) {
        Method computeVerticalScrollOffset = null;
        try {
            computeVerticalScrollOffset = c.getDeclaredMethod("computeVerticalScrollOffset");
        } catch (NoSuchMethodException e) {
            if (c.getSuperclass() != null) {
                computeVerticalScrollOffset = findcomputeVerticalScrollOffsetMethod(c.getSuperclass());
            }
        }

        return computeVerticalScrollOffset;
    }

//    private void scrollBy(float distance) {
//        View child0View = getChildAt(0);
//        View child1View = getChildAt(1);
//        float distanceRemain = 0;
//
//        int child0Top = child0View.getTop();
//        int child0Height = child0View.getHeight();
//        if (distance < 0) {//向上
//            int child1Top = child1View.getTop();
//            int child1Height = child1View.getHeight();
//
//            //child0View 缩小
//            if (-1 != mSrcHeightHead && child0View.getHeight() > mSrcHeightHead) {
//                float off = distance;
//                // distance = 0;
//                if (child0View.getHeight() + distance < mSrcHeightHead) {
//                    // distance = mSrcHeightHead
//                    off = -(child0View.getHeight() - mSrcHeightHead);
//                    distance -= off;
//                } else {
//                    distance = 0;
//                }
//
//                child0View.getLayoutParams().height += off;
//                child1Top += off;  //child0view 缩小的同时, child1view 的高度也会随之上升 这里很重要
//                requestLayout();
//            }
//
//            if (distance != 0) {
//                if (child1Top + child1Height >= getHeight()) {//能整体向上移动或child1View内部向上滚动
//                    if (child1Top + child1Height + distance <= getHeight()) {
//                        distanceRemain = -(distance + (child1Top + child1Height - getHeight()));//正数
//                        distance = -(child1Top + child1Height - getHeight());//负数
//                    } else if (child0Top + child0Height + distance - mStationHeightUp <= 0) {
//                        distanceRemain = -(distance + (child0Top + child0Height - mStationHeightUp));//正数
//                        distance = -(child0Top + child0Height - mStationHeightUp);//负数
//                    }
//                } else {
//                    distance = 0;
//                    distanceRemain = 0;
//                }
//            }
//        } else { //向下
//            if (child1View instanceof RecyclerView) {
//                RecyclerView recyclerView = (RecyclerView) child1View;
//                int nScrollOffset = recyclerView.computeVerticalScrollOffset();
//                //   int nScrollExtent = recyclerView.computeVerticalScrollExtent();
//                //   int nScrollRange = recyclerView.computeVerticalScrollRange();
//                //   int child1Height = child1View.getHeight();
//
//                if (nScrollOffset > 0) {//内部有滚动,那么就要计算内部滚动距离,其他分配给整理滚动
//                    if (nScrollOffset - distance <= 0) {
//                        distanceRemain = -nScrollOffset;
//                        distance = distance - nScrollOffset;
//                    } else {
//                        distanceRemain = -distance;//负数
//                        distance = 0;
//                    }
//                } else {
//                    if (child0Top + distance >= 0) {
//                        int off = (int) (child0Top + distance);
//                        distance = -child0Top;
//
//                        //child0View放大
//                        if (mScaleBig) {//fling不能引起拉伸
//                            child0View.getLayoutParams().height += off;
//                            requestLayout();
//                        }
//                    }
//                }
//            }
//        }
//
//        if (0 != (int) distance) {
//            child0View.offsetTopAndBottom((int) distance);
//            child1View.offsetTopAndBottom((int) distance);
//        }
//        if (0 != (int) distanceRemain) {
//            child1View.scrollBy(0, (int) distanceRemain);//外部无法滚动的时候,内部滚动
//        }
//    }
}
