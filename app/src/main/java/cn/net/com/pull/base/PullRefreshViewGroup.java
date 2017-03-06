package cn.net.com.pull.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Method;

import cn.net.com.dragviews.R;


public class PullRefreshViewGroup extends ViewGroup {
    static private String TAG = "PullRefreshViewGroup";

    private boolean mHasPullRefresh = true;             //有没有上拉刷新
    private boolean mHasLoadMore = true;                //有没有加载更多

    private boolean mPullRefreshLoading = false;       //是否正在下拉刷新加载
    private boolean mLoadingMore = false;               //是否正在加载更多


    private View mHeadView = null;                      //下拉刷新
    private View mTailView = null;                      //上拉加载

    private TextView mTvRefreshInfo = null;             //下拉刷新中文字的提示语

    private int mCanRefreshHeight = 200;                //拉伸到多高就可以启动加载

    private IRefreshLoad mRefreshLoad;                   //界面回调接口


    private int mSrcHeightHead = -1;        //下拉刷新layout的原始高度
    private float mPreY = 0f;
    private boolean mSliding = false;       //是否正在滑动

    private ScrollerCompat mScroller;          //平滑滚动接口

    private int mPreCurY = 0;                  //mScroller平滑滑动过程中的前一个位置点
    private VelocityTracker vTracker = VelocityTracker.obtain();//加速度计算控制

    private Method mComputeVerticalScrollOffset = null;
    private Method mComputeVerticalScrollExtent = null;
    private Method mComputeVerticalScrollRange = null;

    public PullRefreshViewGroup(Context context) {
        super(context);

        init(context);
    }

    public PullRefreshViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public PullRefreshViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullRefreshViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context);
    }

    private void init(Context context) {
        mScroller = ScrollerCompat.create(context, sQuinticInterpolator);
    }

    static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public void addHeadView(View headView) {
        addView(headView, 0);

        mHeadView = headView;
        mTvRefreshInfo = (TextView) mHeadView.findViewById(R.id.tvRefreshInfo);
    }

    public void addTailView(View headView) {
        int index = getChildCount();
        addView(headView, index);

        mTailView = headView;
    }

    public void setCanRefreshHeight(int canRefreshHeight) {
        mCanRefreshHeight = canRefreshHeight;
    }

    public void setRefreshLoadListen(IRefreshLoad refreshLoad) {
        mRefreshLoad = refreshLoad;
    }

    public boolean isPullRefreshLoading(){
        return mPullRefreshLoading;
    }

    public boolean isLoadingMore(){
        return mLoadingMore;
    }

    public void setHasPullRefresh(boolean bHasPullRefresh) {
        mHasPullRefresh = bHasPullRefresh;
        if (!mHasPullRefresh) {
            mSrcHeightHead = 0;

            if (null != mHeadView) {
                removeView(mHeadView);
                mHeadView = null;
                requestLayout();
            }
        }
    }

    public void setHasLoadMore(boolean bHasLoadMore) {
        mHasLoadMore = bHasLoadMore;
        if (!mHasLoadMore) {
            if (null != mTailView) {
                removeView(mTailView);
                mTailView = null;
                requestLayout();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (mHasPullRefresh && null == mHeadView) {//添加下拉刷新的视图
            View headView = LayoutInflater.from(getContext()).inflate(R.layout.pull_refresh_head, null);
            addHeadView(headView);
        }

        int childCount = getChildCount();
        if (childCount <= 2 && null == mTailView) {//添加上拉加载更多的视图
            View taildView = LayoutInflater.from(getContext()).inflate(R.layout.pull_refresh_tail, null);
            addTailView(taildView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthModeParent = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSizeParent = View.MeasureSpec.getSize(widthMeasureSpec);

        int heightModeParent = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSizeParent = View.MeasureSpec.getSize(heightMeasureSpec);

        int midChildIndex = 0;
        if (null != mHeadView) {
            midChildIndex = 1;

            int readWidth = widthSizeParent;
            int readHeight = 250;

            int childheadHeight = mHeadView.getLayoutParams().height;
            if (childheadHeight > 0) {
                readHeight = childheadHeight;
            } else {
                mHeadView.getLayoutParams().height = readHeight;
            }

            mHeadView.measure(View.MeasureSpec.makeMeasureSpec(readWidth, MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(readHeight, MeasureSpec.EXACTLY));
        }

        View midChildView = getChildAt(midChildIndex);

        midChildView.measure(View.MeasureSpec.makeMeasureSpec(widthSizeParent, MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(heightSizeParent, MeasureSpec.EXACTLY));

        if (null != mTailView) {
            int readWidth = widthSizeParent;
            int readHeight = 180;

            int childheadHeight = mTailView.getLayoutParams().height;
            if (childheadHeight > 0) {
                readHeight = childheadHeight;
            } else {
                mTailView.getLayoutParams().height = readHeight;
            }

            mTailView.measure(View.MeasureSpec.makeMeasureSpec(readWidth, MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(readHeight, MeasureSpec.EXACTLY));
        }


        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean mInit = true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int midChildIndex = 0;
        if (null != mHeadView) {
            midChildIndex = 1;


            int headChildLeft = mHeadView.getLeft();
            int headChildTop = mHeadView.getTop();
            int headChildWitdh = mHeadView.getMeasuredWidth();
            int headChildHeight = mHeadView.getMeasuredHeight();
            if (mInit && headChildHeight > 0) {
                mInit = false;
                headChildTop = -headChildHeight;
            }

            mHeadView.layout(headChildLeft, headChildTop, headChildLeft + headChildWitdh, headChildTop + headChildHeight);
        }

        View midChildView = getChildAt(midChildIndex);

        int midChildLeft = midChildView.getLeft();
        int midChildTop = midChildView.getTop();
        int midChildWitdh = midChildView.getMeasuredWidth();
        int midChildHeight = midChildView.getMeasuredHeight();

        midChildView.layout(midChildLeft, midChildTop, midChildLeft + midChildWitdh, midChildTop + midChildHeight);

        if (null != mTailView) {
            int tailChildLeft = mTailView.getLeft();
            int tailChildTop = mTailView.getTop();
            int tailChildWitdh = mTailView.getMeasuredWidth();
            int tailChildHeight = mTailView.getMeasuredHeight();

            int tailChildReadlTop = midChildTop + midChildHeight;// - tailChildHeight;

            mTailView.layout(tailChildLeft, tailChildReadlTop, tailChildLeft + tailChildWitdh, tailChildReadlTop + tailChildHeight);
        }
    }

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

                    if (mSrcHeightHead == -1 && mHasPullRefresh) {
                        View child0View = mHeadView;
                        mSrcHeightHead = child0View.getHeight();
                    }

                    scrollBy(distance);
                }

                mPreY = curY;
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mPreCurY = 0;

                View child0View = mHeadView;
                int child0Height = null != child0View ? child0View.getHeight() : 0;
                int child0Height2 = null != child0View ? child0View.getLayoutParams().height : 0; //视图的最终高度是有这个来决定的,请看onMeasure 函数的实现
                //   int child0Height3 = child0View.getMeasuredHeight();
                if (child0Height2 != child0Height) {
                    child0Height = child0Height2;
                }
                int child0Top = null != child0View ? child0View.getTop() : 0;
                int dy = child0Height - mSrcHeightHead + (mSrcHeightHead - Math.abs(child0Top));
                Log.d(TAG, "onTouchEvent()ACTION_UP child0Height:" + String.valueOf(child0Height) + " mSrcHeightHead:" + String.valueOf(mSrcHeightHead) + " child0Top:" + String.valueOf(child0Top));
                if (dy > 0) {//恢复拉伸视图的位置
                    if (!mLoadingMore && dy > mCanRefreshHeight && child0Top + child0Height2 > mCanRefreshHeight && mRefreshLoad != null) {
                        dy -= mCanRefreshHeight;

                        if (!mPullRefreshLoading) {
                            mPullRefreshLoading = true;
                            mTvRefreshInfo.setText("正在加载...");
                            mRefreshLoad.pullRefreshStartLoad();
                        }
                    }
                    mScroller.startScroll(0, 0, 0, -dy);
                    invalidate();
                } else {
                    vTracker.computeCurrentVelocity(1000);

                    float yvel = vTracker.getYVelocity();
                    if (yvel != 0) {//为了满足内部视图的快速滚动( 中间内容视图 )
                        mScroller.fling(0, 0, 0, (int) yvel, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        invalidate();
                    }
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
        //  super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currY = mScroller.getCurrY();
            int finalY = mScroller.getFinalY();

            float curVelocity = mScroller.getCurrVelocity();


            float distance = currY - mPreCurY;

            Log.d(TAG, "computeScroll() currY:" + String.valueOf(currY) + " finalY:" + String.valueOf(finalY)
                    + " curVelocity:" + String.valueOf(curVelocity)
                    + " distance:" + String.valueOf(distance));

            if (distance != 0) {
                scrollBy(distance);
            }

            mPreCurY = currY;


            View midView = getChildAt(null != mHeadView ? 1 : 0);

            int bottomChild1 = midView.getBottom();
            int topChild0 = null != mHeadView ? mHeadView.getTop() : 0;
            boolean bConsumFull = false;

            if (distance < 0) {
                if (!midView.canScrollVertically(1) && getHeight() == bottomChild1) {
                    bConsumFull = true;
                } else if (mTailView != null && mTailView.getBottom() == getHeight()) {
                    bConsumFull = true;
                }
            } else if (distance > 0) {
                if (topChild0 == 0 && !midView.canScrollVertically(-1)) {
                    bConsumFull = true;
                }
            }


            if (mScroller.isFinished() || bConsumFull) {
                mPreCurY = 0;
                mScroller.abortAnimation();
            } else {
                invalidate();
            }
        }
    }

    private void scrollBy(float distance) {
        View child0View = mHeadView;
        View child1View = getChildAt(null == mHeadView ? 0 : 1);
        float distanceRemain = 0;

        int child0Top = null != child0View ? child0View.getTop() : 0;
        // int child0Height = child0View.getHeight();

        if (distance < 0) {//向上
            int child1Top = child1View.getTop();
            //    int child1Height = child1View.getHeight();

            //child0View 缩小
            if (-1 != mSrcHeightHead && null != child0View && child0View.getHeight() > mSrcHeightHead) {
                float off = distance;
                if (child0View.getHeight() + distance < mSrcHeightHead) {
                    off = -(child0View.getHeight() - mSrcHeightHead);
                    distance -= off;
                } else {
                    distance = 0;
                }

                child0View.getLayoutParams().height += (int) off;
                child1Top += (int) off;  //child0view 缩小的同时, child1view 的高度也会随之上升 这里很重要
                requestLayout();
                child1View.offsetTopAndBottom((int) off);
                if (null != mTailView) {
                    mTailView.offsetTopAndBottom((int) off);
                }
            }

            if (distance != 0) {
                if (child0Top + mSrcHeightHead + distance <= 0) {
                    distanceRemain = -(distance + (child0Top + mSrcHeightHead));//正数
                    distance = -(child0Top + mSrcHeightHead);//负数
                }

                //可以显示加载更多吗?
                boolean bDirectDown = false;
                boolean bCanScroll = child1View.canScrollVertically(1) || child1View.canScrollVertically(-1);
                if (!bCanScroll) {
                    int child1ChildCount = 0;
                    if (child1View instanceof ViewGroup) {
                        child1ChildCount = ((ViewGroup) child1View).getChildCount();
                    }
                    if (child1ChildCount > 0) {
                        ViewGroup viewGroupChild1 = (ViewGroup) child1View;

                        View child1LastItem = viewGroupChild1.getChildAt(child1ChildCount - 1);
                        int child1ViewBottom = viewGroupChild1.getBottom();
                        int child1LastItemBottom = child1LastItem.getBottom() + child1Top;  //相对于 ImageScaleViewGroup 的位置
                        //增加 child1ViewBottom > getHeight()  来控制 ScrollView
                        if (child1LastItemBottom == getHeight()) {
                            bDirectDown = true;
                        }
                    }
                }
                //正在下拉刷新的时候,不能显示加载更多
                if ((bCanScroll || bDirectDown) && null != mTailView && !mPullRefreshLoading) {

                    int nVerticalScrollExtent = 0, nVerticalScrollRange = 0, nVerticalScrollOffset = 0;
                    Class c = null;

                    try {
                        c = Class.forName(child1View.getClass().getName());
                    } catch (Exception ex) {

                    }
                    try {
                        if (null == mComputeVerticalScrollExtent) {
                            Method computeVerticalScrollExtent = findcomputeVerticalMethod(c, "computeVerticalScrollExtent");
                            computeVerticalScrollExtent.setAccessible(true);
                            mComputeVerticalScrollExtent = computeVerticalScrollExtent;
                        }
                        nVerticalScrollExtent = (int) mComputeVerticalScrollExtent.invoke(child1View);
                    } catch (Exception ex) {

                    }
                    try {
                        if (null == mComputeVerticalScrollRange) {
                            Method computeVerticalScrollRange = findcomputeVerticalMethod(c, "computeVerticalScrollRange");
                            computeVerticalScrollRange.setAccessible(true);
                            mComputeVerticalScrollRange = computeVerticalScrollRange;
                        }

                        nVerticalScrollRange = (int) mComputeVerticalScrollRange.invoke(child1View);
                    } catch (Exception ex) {

                    }
                    try {
                        if (null == mComputeVerticalScrollOffset) {
                            Method computeVerticalScrollOffset = findcomputeVerticalMethod(c, "computeVerticalScrollOffset");
                            computeVerticalScrollOffset.setAccessible(true);
                            mComputeVerticalScrollOffset = computeVerticalScrollOffset;
                        }
                        nVerticalScrollOffset = (int) mComputeVerticalScrollOffset.invoke(child1View);
                    } catch (Exception ex) {

                    }


                    int range = nVerticalScrollRange - nVerticalScrollExtent;
                    if (nVerticalScrollOffset + distanceRemain > range) {
                        float nOff = distanceRemain - (range - nVerticalScrollOffset);

                        distanceRemain = range - nVerticalScrollOffset;
                        distance -= nOff;
                    }

                    int child3Bottom = mTailView.getBottom();

                    if (child3Bottom + distance < getHeight()) {
                        distance = getHeight() - child3Bottom;
                    }
                }

                if (!bCanScroll) {
                    distanceRemain = 0;
                }
            }

        } else {//向下
            int nScrollOffset = 0;
            try {
                Class c = Class.forName(child1View.getClass().getName());
                Method computeVerticalScrollOffset = findcomputeVerticalMethod(c, "computeVerticalScrollOffset");//c.getDeclaredMethod("computeVerticalScrollOffset");
                computeVerticalScrollOffset.setAccessible(true);
                nScrollOffset = (int) computeVerticalScrollOffset.invoke(child1View);
            } catch (Exception ex) {

            }

            int child2Top = null != mTailView ? mTailView.getTop() : getHeight();//注意默认值
            if (child2Top < getHeight()) {
                if (child2Top + distance > getHeight()) {
                    distanceRemain = distance - (getHeight() - child2Top);
                    distance = getHeight() - child2Top;
                }
            } else if (nScrollOffset > 0) {//内部有滚动,那么就要计算内部滚动距离,其他分配给整体滚动
                if (nScrollOffset - distance <= 0) {
                    distanceRemain = -nScrollOffset;
                    //   distance = distance - nScrollOffset;
                    distance = 0;  //内部能滚动,不让外部去滚动
                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();  //内部滚动完后,立即停止
                    }
                } else {
                    distanceRemain = -distance;//负数
                    distance = 0;
                }
            } else {
                if (child0Top + distance > 0) {//child0放大,child1移动
                    int off = (int) (child0Top + distance);
                    distance = -child0Top;

                    if (null != child0View) {
                        child0View.getLayoutParams().height += off;
                        requestLayout();
                    } else {
                        off = 0;
                    }

                    child1View.offsetTopAndBottom(off);
                    if (null != mTailView) {
                        mTailView.offsetTopAndBottom(off);
                    }
                }
            }
        }

        if (0 != (int) distance) {
            if (null != child0View) {
                child0View.offsetTopAndBottom((int) distance);
            }
            child1View.offsetTopAndBottom((int) distance);
            if (null != mTailView) {
                mTailView.offsetTopAndBottom((int) distance);
            }
        }

        scrollByForMidView(distanceRemain);//外部无法滚动的时候,内部滚动

        if (!mPullRefreshLoading && !mLoadingMore) {
            int tailviewTop = null != mTailView ? mTailView.getTop() : getHeight();//注意默认值

            if (tailviewTop < getHeight() && mHasLoadMore) {//加载更多
                mLoadingMore = true;
                if (mRefreshLoad != null) {
                    mRefreshLoad.pullUpStartLoadMore();
                }
            } else {
                if (mHasPullRefresh) {
                    if (distance < 0) {
                        int child0Bottom = child0View.getBottom();
                        if (child0Bottom < mCanRefreshHeight) {
                            mTvRefreshInfo.setText("下拉刷新");
                        }
                    } else {
                        int child0Bottom = child0View.getBottom();
                        if (child0Bottom > mCanRefreshHeight) {
                            mTvRefreshInfo.setText("松开刷新");
                        }
                    }
                }
            }
        }
    }

    private void scrollByForMidView(float distanceRemain) {
        if (0 != (int) distanceRemain) {
            View child1View = getChildAt(null == mHeadView ? 0 : 1);
            if (child1View instanceof ListView) {
                ((ListView) child1View).smoothScrollBy((int) distanceRemain, 0);
            } /*else if (child1View instanceof ScrollView){
                ((ScrollView) child1View).smoothScrollBy(0,(int) distanceRemain);
            }*/ else {
                child1View.scrollBy(0, (int) distanceRemain);
            }
        }
    }

    private Method findcomputeVerticalMethod(Class c, String methodName, Class<?>... parameterTypes) {
        Method computeVerticalScrollOffset = null;

        try {
            computeVerticalScrollOffset = c.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            if (c.getSuperclass() != null) {
                computeVerticalScrollOffset = findcomputeVerticalMethod(c.getSuperclass(), methodName, parameterTypes);
            }
        }

        return computeVerticalScrollOffset;
    }

    public void setPullRefreshLoadComplete() {
        mPreCurY = 0;
        mScroller.abortAnimation();


        View child0View = getChildAt(0);
        int child0Height = child0View.getLayoutParams().height;

        int child0Top = child0View.getTop();
        int dy = child0Height - mSrcHeightHead + (mSrcHeightHead - Math.abs(child0Top));
        Log.d(TAG, "setPullRefreshLoadComplete() child0Height:" + String.valueOf(child0Height) + " mSrcHeightHead:" + String.valueOf(mSrcHeightHead) + " child0Top:" + String.valueOf(child0Top));
        if (dy > 0) {//恢复拉伸视图的位置
            mScroller.startScroll(0, 0, 0, -dy);
            invalidate();
        }

        mPullRefreshLoading = false;
    }

    public void setPullUpLoadMoreComplete() {
        //为了兼容ScrollView使用异步，这样 ScrollView 就不会出现明显的滑动现象
        //不同手机可能兼容性会不一样，有待验证
        getChildAt(0).post(new Runnable() {
            @Override
            public void run() {
                endPullUpLoadMore();
            }
        });
        //  endPullUpLoadMore();
    }

    public void endPullUpLoadMore() {
        mPreCurY = 0;
        mScroller.abortAnimation();

        if (null != mTailView) {
            int tailviewTop = mTailView.getTop();
            int pHeight = getHeight();
            int dy = pHeight - tailviewTop;
            if (dy > 0) {//立即滚动
                int midIndex = 0;
                if (null != mHeadView) {
                    midIndex++;
                    mHeadView.offsetTopAndBottom(dy);
                }
                getChildAt(midIndex).offsetTopAndBottom(dy);
                scrollByForMidView(dy);
                if (null != mTailView) {
                    mTailView.offsetTopAndBottom(dy);
                }
            }
        }

        mLoadingMore = false;
    }

    public interface IRefreshLoad {
        void pullRefreshStartLoad();

        void pullUpStartLoadMore();
    }
}
