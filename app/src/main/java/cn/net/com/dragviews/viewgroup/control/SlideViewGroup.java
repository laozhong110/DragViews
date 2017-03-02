package cn.net.com.dragviews.viewgroup.control;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;


public class SlideViewGroup extends ViewGroup {

    String TAG = "SlideViewGroup";

    public final static int DOCK_LEFT = 1;
    public final static int DOCK_MID = 2;
    public final static int DOCK_RIGHT = 3;


    private int mLeftWidth = 380;
    private int mMaxOffset = 80;  //底下的视图左偏移
    private int mTouchWidth = 160;//控制触摸的范围(最上面视图的左边缘的距离)

    private int mDockType = DOCK_LEFT;

    private float mPreXpos = -1;


    private int mCurOffset = -1;
    private boolean mBscroll = false;

    private boolean mSliding = false;  //是否正在滑动

    private Scroller mScroller;
    private OnSlideListen mOnSlideListen;

    public SlideViewGroup(Context context) {
        super(context);
        init(context);
    }

    public SlideViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlideViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }

    public void setOnSlideListen(OnSlideListen onSlideListen){
        mOnSlideListen = onSlideListen;
    }

    public void setLeftWidth(int leftWidth){
        mLeftWidth = leftWidth;
    }

    public void setMaxOffset(int maxOffset){
        mMaxOffset = maxOffset;
    }

    public void setTouchWidth(int nTouchWidth){
        mTouchWidth = nTouchWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalStateException("the child~s count must is two");
        }

        View viewChild1 = getChildAt(0);
        View viewChild2 = getChildAt(1);

        ViewGroup.LayoutParams lpChild1 = viewChild1.getLayoutParams();
        ViewGroup.LayoutParams lpChild2 = viewChild2.getLayoutParams();

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        int child1MeasureWidth = viewChild1.getMeasuredWidth();
        int child1MeasureHeight = viewChild1.getMeasuredHeight();

        int child2MeasureWidth = viewChild2.getMeasuredWidth();
        int child2MeasureHeight = viewChild2.getMeasuredHeight();


        int child1LpHeight = lpChild1.height;
        int child1LpWidth = lpChild1.width;

        int child1RealWidthMode = widthMode;
        int child1RealWidthSize = widthSize;

        int child1RealHeightMode = heightMode;
        int child1RealHeightSize = heightSize;


        if (child1LpWidth == LayoutParams.MATCH_PARENT) {
            child1RealWidthMode = MeasureSpec.EXACTLY;
            child1RealWidthSize = widthSize;
        } else if (child1LpWidth == LayoutParams.WRAP_CONTENT) {
            child1RealWidthMode = MeasureSpec.EXACTLY;
            child1RealWidthSize = widthSize;
        } else if (child1LpWidth > 0) {
            child1RealWidthMode = MeasureSpec.EXACTLY;
            child1RealWidthSize = child1LpWidth;
        } else {
            child1RealWidthMode = MeasureSpec.UNSPECIFIED;
            child1RealWidthSize = widthSize;
        }

        if (child1LpHeight == LayoutParams.MATCH_PARENT) {
            child1RealHeightMode = MeasureSpec.EXACTLY;
            child1RealHeightSize = heightSize;
        } else if (child1LpHeight == LayoutParams.WRAP_CONTENT) {
            child1RealHeightMode = MeasureSpec.EXACTLY;
            child1RealHeightSize = heightSize;
        } else if (child1LpHeight > 0) {
            child1RealHeightMode = MeasureSpec.EXACTLY;
            child1RealHeightSize = child1LpHeight;
        } else {
            child1RealHeightMode = MeasureSpec.UNSPECIFIED;
            child1RealHeightSize = heightSize;
        }

        viewChild1.measure(MeasureSpec.makeMeasureSpec(child1RealWidthSize, child1RealWidthMode),
                MeasureSpec.makeMeasureSpec(child1RealHeightSize, child1RealHeightMode));

        int child2LpHeight = lpChild2.height;
        int child2LpWidth = lpChild2.width;

        int child2RealWidthMode = widthMode;
        int child2RealWidthSize = widthSize;

        int child2RealHeightMode = heightMode;
        int child2RealHeightSize = heightSize;


        if (child2LpWidth == LayoutParams.MATCH_PARENT) {
            child2RealWidthMode = MeasureSpec.EXACTLY;
            child2RealWidthSize = widthSize;
        } else if (child2LpWidth == LayoutParams.WRAP_CONTENT) {
            child2RealWidthMode = MeasureSpec.EXACTLY;
            child2RealWidthSize = widthSize;
        } else if (child2LpWidth > 0) {
            child2RealWidthMode = MeasureSpec.EXACTLY;
            child2RealWidthSize = child2LpWidth;
        } else {
            child2RealWidthMode = MeasureSpec.UNSPECIFIED;
            child2RealWidthSize = widthSize;
        }

        if (child2LpHeight == LayoutParams.MATCH_PARENT) {
            child2RealHeightMode = MeasureSpec.EXACTLY;
            child2RealHeightSize = heightSize;
        } else if (child2LpHeight == LayoutParams.WRAP_CONTENT) {
            child2RealHeightMode = MeasureSpec.EXACTLY;
            child2RealHeightSize = heightSize;
        } else if (child2LpHeight > 0) {
            child2RealHeightMode = MeasureSpec.EXACTLY;
            child2RealHeightSize = child2LpHeight;
        } else {
            child2RealHeightMode = MeasureSpec.UNSPECIFIED;
            child2RealHeightSize = heightSize;
        }
        viewChild2.measure(MeasureSpec.makeMeasureSpec(child2RealWidthSize, child2RealWidthMode),
                MeasureSpec.makeMeasureSpec(child2RealHeightSize, child2RealHeightMode));


//        viewChild1.measure(MeasureSpec.makeMeasureSpec(300,MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(300,MeasureSpec.EXACTLY));
//
//        viewChild2.measure(MeasureSpec.makeMeasureSpec(300,MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(300,MeasureSpec.EXACTLY));

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalStateException("the child~s count must is two");
        }

        View viewChild1 = getChildAt(0);
        View viewChild2 = getChildAt(1);

        int child1MeasureWidth = viewChild1.getMeasuredWidth();
        int child1MeasureHeight = viewChild1.getMeasuredHeight();

        int child2MeasureWidth = viewChild2.getMeasuredWidth();
        int child2MeasureHeight = viewChild2.getMeasuredHeight();

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();


        int ww = viewChild1.getWidth();

//        viewChild1.layout(0, 0, child1MeasureWidth, child1MeasureHeight);
//        viewChild2.layout(0, height - child2MeasureHeight, child2MeasureWidth, height - child2MeasureHeight + child2MeasureHeight);

        int leftChild1 = viewChild1.getLeft();
        int leftChild2 = viewChild2.getLeft();
        if (ww == 0) {
            viewChild1.layout(-mMaxOffset, 0, -mMaxOffset + child1MeasureWidth, child1MeasureHeight);  //初次定位
        }else{//子控件变化可能引起的重新定位
            viewChild1.layout(leftChild1, 0, leftChild1 + child1MeasureWidth, child1MeasureHeight);  //后面滑动等的重新定位
        }

        viewChild2.layout(leftChild2, height - child2MeasureHeight, leftChild2 + child2MeasureWidth, height - child2MeasureHeight + child2MeasureHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean ret = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "onInterceptTouchEvent ACTION_DOWN");
              //   ret = true;
                mPreXpos = event.getX();
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                Log.d(TAG, "onInterceptTouchEvent ACTION_MOVE");

                float curXpos = event.getX();

                //处理滑动冲突
                float distance = curXpos - mPreXpos;
                int nTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if (Math.abs(distance) >= nTouchSlop){
                    mSliding = ret = true;
                }else{
                    mSliding = ret = false;
                }
            }
            break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                Log.d(TAG, "onInterceptTouchEvent ACTION_UP");
                ret = mSliding;
            }
            break;
        }
        return ret ? true : super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "onTouchEvent ACTION_DOWN");

                mPreXpos = event.getX();
                View viewChild2 = getChildAt(1);
                int leftchild2 = viewChild2.getLeft();
                float maxTouchWidth = leftchild2 - mPreXpos;
                if (Math.abs(maxTouchWidth) <= mTouchWidth){
                    ret = true;
                }else{
                    ret = false;
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                Log.d(TAG, "onTouchEvent ACTION_MOVE");
                float curXpos = event.getX();

                float distance = curXpos - mPreXpos;

                View viewChild2 = getChildAt(1);
                int leftchild2 = viewChild2.getLeft();
                int widthchild2 = viewChild2.getWidth();
                int topChild2 = viewChild2.getTop();
                int heightChild2 = viewChild2.getHeight();

                int curLeft = (int) (leftchild2 + distance);

                int parentWidth = getWidth();

                switch (mDockType) {
                    case DOCK_LEFT: {
                        if (curLeft < 0) {
                            curLeft = 0;
                        } else if (curLeft > mLeftWidth) {
                            curLeft = mLeftWidth;
                        }
                    }
                    break;
                    case DOCK_MID: {
                        if (curLeft < 0) {
                            curLeft = 0;
                        } else if (curLeft > parentWidth) {
                            curLeft = parentWidth;
                        }
                    }
                    break;
                    case DOCK_RIGHT: {
                        if (curLeft < mLeftWidth) {
                            curLeft = mLeftWidth;
                        } else if (curLeft > parentWidth) {
                            curLeft = parentWidth;
                        }
                    }
                    break;
                }

                viewChild2.layout(curLeft, topChild2, curLeft + widthchild2, topChild2 + heightChild2);

                //view1进行相应的移动
                layoutForViewChild1(curLeft);

                mPreXpos = curXpos;
            }
            break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                Log.d(TAG, "onTouchEvent ACTION_UP");

                View viewChild2 = getChildAt(1);
                int leftchild2 = viewChild2.getLeft();
                int widthchild2 = viewChild2.getWidth();
                int topChild2 = viewChild2.getTop();
                int heightChild2 = viewChild2.getHeight();

                int parentWidth = getWidth();

                int distance = 0;

                switch (mDockType) {
                    case DOCK_LEFT: {
                        if (leftchild2 < mLeftWidth / 2) {//往左
                            distance = -leftchild2;
                            mDockType = DOCK_LEFT;
                        } else {
                            distance = mLeftWidth - leftchild2;
                            mDockType = DOCK_MID;
                        }
                    }
                    break;
                    case DOCK_MID: {
                        if (leftchild2 < mLeftWidth) {//左半边
                            if (leftchild2 < mLeftWidth / 2) {//往左
                                distance = -leftchild2;
                                mDockType = DOCK_LEFT;
                            } else {
                                distance = mLeftWidth - leftchild2;
                                mDockType = DOCK_MID;
                            }

                        } else if (leftchild2 > mLeftWidth) {//右半边
                            if (leftchild2 - mLeftWidth < (parentWidth - mLeftWidth) / 2) {//往左
                                distance = mLeftWidth - leftchild2;
                                mDockType = DOCK_MID;
                            } else {
                                distance = parentWidth - leftchild2;
                                mDockType = DOCK_RIGHT;
                            }
                        } else {
                            distance = 0;
                            mDockType = DOCK_MID;
                        }
                    }
                    break;
                    case DOCK_RIGHT: {
                        if (leftchild2 - mLeftWidth < (parentWidth - mLeftWidth) / 2) {//往左
                            distance = mLeftWidth - leftchild2;
                            mDockType = DOCK_MID;
                        } else {
                            distance = parentWidth - leftchild2;
                            mDockType = DOCK_RIGHT;
                        }
                    }
                    break;
                }

                mBscroll = true;
                mScroller.startScroll(leftchild2, topChild2, distance, 0);
                invalidate();

            }
            break;
        }
        return ret ? true : super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        //super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();

            View viewChild2 = getChildAt(1);
            int leftchild2 = viewChild2.getLeft();
            int widthchild2 = viewChild2.getWidth();
            int topChild2 = viewChild2.getTop();
            int heightChild2 = viewChild2.getHeight();

            Log.d(TAG, "currX:" + String.valueOf(currX) + ",leftchild2:" + String.valueOf(leftchild2));

            //viewChild2.layout(currX, topChild2, currX + widthchild2, topChild2 + heightChild2);

            int left = getLeft();
            int distance = currX + left;
            if (mCurOffset == -1) {
                mCurOffset = currX;
            }
            distance = currX - mCurOffset;
            mCurOffset = currX;
            viewChild2.offsetLeftAndRight(distance);

            layoutForViewChild1(viewChild2.getLeft());

            invalidate();
        } else {
            if (mBscroll) {
                mBscroll = false;
                mCurOffset = -1;
                View viewChild2 = getChildAt(1);
                int left = viewChild2.getLeft();

                switch (mDockType) {
                    case DOCK_LEFT: {
                        if (left > 0) {
                            viewChild2.offsetLeftAndRight(-left);
                        }
                        mHandler.obtainMessage().sendToTarget();
                    }
                    break;
                    case DOCK_MID: {
                        if (left != mLeftWidth) {
                            viewChild2.offsetLeftAndRight(mLeftWidth - left);
                        }
                        mHandler.obtainMessage().sendToTarget();
                    }
                    break;
                    case DOCK_RIGHT: {
                        int pwidth = getWidth();
                        if (left != pwidth) {
                            viewChild2.offsetLeftAndRight(pwidth - left);
                        }
                        mHandler.obtainMessage().sendToTarget();
                    }
                    break;
                }
            }
        }
    }

    private void layoutForViewChild1(int curLeftViewChild2){
        int curLeftChild1 = 1;
        View viewChild1 = getChildAt(0);
        int leftchild1 = viewChild1.getLeft();
        int widthchild1 = viewChild1.getWidth();
        int topChild1 = viewChild1.getTop();
        int heightChild1 = viewChild1.getHeight();
        if (curLeftViewChild2 == 0) {
            curLeftChild1 = -mMaxOffset;
        } else if (curLeftViewChild2 > 0 && curLeftViewChild2 < mLeftWidth) {
            curLeftChild1 = -(mMaxOffset-mMaxOffset*curLeftViewChild2/mLeftWidth);
        } else if (curLeftViewChild2 == mLeftWidth) {
            curLeftChild1 = 0;
        }
        if (curLeftChild1 != -1) {
            viewChild1.layout(curLeftChild1, topChild1, curLeftChild1 + widthchild1, topChild1 + heightChild1);
        }
    }

    public interface OnSlideListen{
        void onSlideFinish(int nDockType);
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
