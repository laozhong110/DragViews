package cn.net.com.dragviews.imageup;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;


public class ImageBehaviour<V extends View> extends CoordinatorLayout.Behavior<View> {
    String TAG = "ImageBehaviour";

    int mImageViweSrcHeight = -1;

  //  V pp;

    public ImageBehaviour() {
        super();
    }

    public ImageBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);

//        if (pp instanceof RecyclerView){
//            int x = 0;
//            x++;
//        }

        mAnimateRunable = new AnimateRunable(context);
        mPictureRecovyAnimateRunable = new PictureRecovyAnimateRunable(context);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll()");
        //  return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);;
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);

        Log.d(TAG, "onNestedScrollAccepted()");
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

        if (target instanceof RecyclerView) {
            RecyclerView nestedScrollView = (RecyclerView) target;
            boolean bCanScroll = nestedScrollView.canScrollVertically(dy);//某个方向不能滚动
            //  boolean bScrollUp = nestedScrollView.canScrollVertically(-1);//向上

            boolean fullCanScroll = bCanScroll?true:nestedScrollView.canScrollVertically(-dy);//是否可用滚动,无论是向上还是向下
           // int nScrollRange = nestedScrollView.computeVerticalScrollRange();
           // int nScrollExtent = nestedScrollView.computeVerticalScrollExtent();


            View child0View = coordinatorLayout.getChildAt(0);
            int child0Top = child0View.getTop();
            int child0Height = child0View.getHeight();


            if (mImageViweSrcHeight == -1) {
                mImageViweSrcHeight = child0Height;
            }


            if (bCanScroll && dy > 0 && child0Top + child0Height >= 0) {//整体往上
                if (child0Top + child0Height - dy > 0) {
                    int offset = consumed[1] = dy;
                    if (-1 != mImageViweSrcHeight && child0View.getLayoutParams().height > mImageViweSrcHeight) {//向上压缩
                        if (child0View.getLayoutParams().height - dy >= mImageViweSrcHeight) {
                            child0View.getLayoutParams().height -= dy;
                            coordinatorLayout.requestLayout();
                        } else if (child0View.getLayoutParams().height - dy < mImageViweSrcHeight) {

                            child0View.getLayoutParams().height = mImageViweSrcHeight;
                            coordinatorLayout.requestLayout();

//                            offset = dy - (child0View.getLayoutParams().height - mImageViweSrcHeight);
//                            child0View.offsetTopAndBottom(-offset);
//                            target.offsetTopAndBottom(-offset);
                        }
                    } else {
                        child0View.offsetTopAndBottom(-offset);
                        target.offsetTopAndBottom(-offset);
                    }
                } else if (child0Top + child0Height - dy <= 0) {
                    //consumed[1] = dy - (child0Top + child0Height);
                    int offset = consumed[1] = (child0Top + child0Height);

                    // int offset = -(child0Top + child0Height);
                    child0View.offsetTopAndBottom(-offset);
                    target.offsetTopAndBottom(-offset);

//                    int offset = -(child0Top + child0Height);
//                    child0View.offsetTopAndBottom(offset);
//                    target.offsetTopAndBottom(offset);
                }
            } /*else if (!bCanScroll && child0Top < 0 && dy < 0) {//整体往下,这里没有往下拉伸
                if (child0Top - dy < 0) {
                    int offset = consumed[1] = dy;

                    child0View.offsetTopAndBottom(-offset);
                    target.offsetTopAndBottom(-offset);
                } else if (child0Top - dy > 0) {
                    consumed[1] = dy - child0Top;

                    int offset = -(child0Top);
                    child0View.offsetTopAndBottom(offset);
                    target.offsetTopAndBottom(offset);
                }
            }*/
            else if (!bCanScroll && child0Top <= 0 && dy < 0) {//整体往下,同时往下拉伸
                if (child0Top - dy < 0) {
                    int offset = dy;
                    child0View.offsetTopAndBottom(-offset);
                    target.offsetTopAndBottom(-offset);
                } else if (child0Top - dy >= 0) {
                    int offset = -(child0Top);
                    if (offset == 0) {//开始拉伸
                        child0View.getLayoutParams().height += (-dy);
                        coordinatorLayout.requestLayout();
                    } else {
                        child0View.offsetTopAndBottom(offset);
                        target.offsetTopAndBottom(offset);
                    }
                }

                consumed[1] = dy;  //因为RecyclerView已经无法往下滚动了,所有这里必须全部消费可滚动的高度
            }else if (!fullCanScroll && dy > 0 && child0Top + child0Height >= 0){//往上滑动
                consumed[1] = dy;

                if (-1 != mImageViweSrcHeight && child0View.getLayoutParams().height > mImageViweSrcHeight) {//向上压缩
                    if (child0View.getLayoutParams().height - dy >= mImageViweSrcHeight) {
                        child0View.getLayoutParams().height -= dy;
                        coordinatorLayout.requestLayout();
                    } else if (child0View.getLayoutParams().height - dy < mImageViweSrcHeight) {
                        child0View.getLayoutParams().height = mImageViweSrcHeight;
                        coordinatorLayout.requestLayout();
                    }
                } else {
                    RecyclerView recyclerView = (RecyclerView) target;

                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int childCount = recyclerView.getChildCount();
                    int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (childCount > 0 && lastCompletelyVisibleItemPosition >= childCount-1){
                        View lastItemView = recyclerView.getChildAt(lastCompletelyVisibleItemPosition);
                        int lastItemBottom = lastItemView.getBottom();
                        int child1Top = recyclerView.getTop();
                      //  int child1Height = recyclerView.getHeight();
                     //   int child1Bottom = recyclerView.getBottom();
                        int totalHeight = coordinatorLayout.getHeight();
                      //  int tempTop = recyclerView.getChildAt(0).getTop();

                        if (lastItemBottom + child1Top > totalHeight){//可以往上移动
                            if (lastItemBottom + child1Top - dy > totalHeight){
                                int offset = dy;
                                child0View.offsetTopAndBottom(-offset);
                                target.offsetTopAndBottom(-offset);
                            }else if (lastItemBottom + child1Top - dy <= totalHeight){
                                int offset = lastItemBottom + child1Top - totalHeight;
                                child0View.offsetTopAndBottom(-offset);
                                target.offsetTopAndBottom(-offset);
                            }
                        }
                    }
                }
            }
        }
        // consumed[1] = dy;
        Log.d(TAG, "onNestedPreScroll()" + " dy:" + String.valueOf(dy) + " consumed[1]:" + String.valueOf(consumed[1]));
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        Log.d(TAG, "onNestedScroll()" + " dyConsumed:" + String.valueOf(dyConsumed) + " dyUnconsumed:" + String.valueOf(dyUnconsumed));
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);

        Log.d(TAG, "onStopNestedScroll()");


        View child0View = coordinatorLayout.getChildAt(0);
    //    int child0Top = child0View.getTop();
     //   int child0Height = child0View.getHeight();
        if (-1 != mImageViweSrcHeight && child0View.getLayoutParams().height > mImageViweSrcHeight) {
            mPictureRecovyAnimateRunable.startScroll(coordinatorLayout, 0, child0View.getLayoutParams().height, 0, mImageViweSrcHeight - child0View.getLayoutParams().height);

//            child0View.getLayoutParams().height = mImageViweSrcHeight;
//            coordinatorLayout.requestLayout();
        }
    }

 //   int preOffset = 0;

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, final View target, final float velocityX, final float velocityY) {
        Log.d(TAG, "onNestedPreFling()" + " velocityY:" + String.valueOf(velocityY));

        boolean bret = false;


        boolean fullCanScroll = false;
        RecyclerView recyclerView = (RecyclerView) target;

        fullCanScroll = recyclerView.canScrollVertically(1);
        fullCanScroll = fullCanScroll?true:recyclerView.canScrollVertically(-1);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int childCount = recyclerView.getChildCount();
        int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        if (!fullCanScroll && childCount > 0 && lastCompletelyVisibleItemPosition >= childCount-1){
            View lastItemView = recyclerView.getChildAt(lastCompletelyVisibleItemPosition);
            int lastItemBottom = lastItemView.getBottom();
            int child1Top = recyclerView.getTop();
            int totalHeight = coordinatorLayout.getHeight();

            if (lastItemBottom + child1Top > totalHeight){//可以往上移动
                fullCanScroll = true;
            }
        }

        if (fullCanScroll) {
            bret = mAnimateRunable.fling(coordinatorLayout, 0, 0, (int) velocityX, (int) velocityY,
                    Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        return true;
    }

    AnimateRunable mAnimateRunable;

    //fling动画
    class AnimateRunable implements Runnable {
        String TAG = "AnimateRunable";
        private ScrollerCompat mScroller;
        View view;
        int mPrePosition = 0;
        boolean mFinished = false;

        boolean isAnimating = false;

        public AnimateRunable(Context context) {
            mScroller = ScrollerCompat.create(context, sQuinticInterpolator);
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                int currY = mScroller.getCurrY();//向上  正  ;  向下  负
                float currVelocity = mScroller.getCurrVelocity();

                Log.d(TAG, "run() scrolling currY:" + String.valueOf(currY) + " currVelocity:" + String.valueOf(currVelocity));

                ViewGroup viewGroup = (ViewGroup) view;
                View child0View = viewGroup.getChildAt(0);
                View child1View = viewGroup.getChildAt(1);

                int child0Top = child0View.getTop();
                int child0Height = child0View.getHeight();

                if (currY > 0) {//往上
                    if (child0Top + child0Height > 0) {

                        RecyclerView recyclerView = (RecyclerView) child1View;

                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int childCount = recyclerView.getChildCount();
                        int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                        if (childCount > 0 && lastCompletelyVisibleItemPosition >= childCount-1) {//处理RecyclerView 的item不足够多的情况,比如没有或只有一两个,即内部不可以滑动
                            View lastItemView = recyclerView.getChildAt(lastCompletelyVisibleItemPosition);
                            int lastItemBottom = lastItemView.getBottom();
                            int child1Top = recyclerView.getTop();
                            int totalHeight = viewGroup.getHeight();

                            if (child1Top + lastItemBottom > totalHeight){
                                int curOffset = currY - mPrePosition;

                                if (lastItemBottom + child1Top - curOffset > totalHeight){
                                    int offset = curOffset;
                                    child0View.offsetTopAndBottom(-offset);
                                    child1View.offsetTopAndBottom(-offset);
                                }else if (lastItemBottom + child1Top - curOffset <= totalHeight){
                                    int offset = lastItemBottom + child1Top - totalHeight;
                                    child0View.offsetTopAndBottom(-offset);
                                    child1View.offsetTopAndBottom(-offset);
                                }
                            }else {
                                mFinished = true;
                            }
                        }else {
                            int curOffset = currY - mPrePosition;
                            if (child0Top + child0Height - curOffset <= 0) {
                                curOffset = child0Top + child0Height;
                            }

                            child0View.offsetTopAndBottom(-curOffset);
                            child1View.offsetTopAndBottom(-curOffset);
                        }
                    } else {
                        mFinished = true;
                        ((RecyclerView) child1View).fling(0, (int) currVelocity);
                    }
                } else if (currY < 0) { //往下

                    RecyclerView recyclerView = ((RecyclerView) child1View);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();


                    boolean bScrolled = child1View.canScrollVertically(1);
                    //先让RecyclerView内部进行滚动,滚动完后再继续整理滚动,是否要继续滚动,只要Scroller没有停止回调 isAnimating来控制内部是否滚动完
                    if (!isAnimating && firstCompletelyVisibleItemPosition != 0 && bScrolled) {
                        //   mFinished = true;
                        isAnimating = true;
                        ((RecyclerView) child1View).fling(0, (int) -currVelocity);
                        ((RecyclerView) child1View).addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                //    int x = 0;
                                //    x++;

                                    isAnimating = false;
                                }
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                            }
                        });

                    } else if (!isAnimating && firstCompletelyVisibleItemPosition == 0) {
                        if (child0Top < 0) {
                            int curOffset = currY - mPrePosition;
                            if (child0Top - curOffset >= 0) {
                                curOffset = child0Top;
                            }

                            child0View.offsetTopAndBottom(-curOffset);
                            child1View.offsetTopAndBottom(-curOffset);
                        }
                    }
                }


                mPrePosition = currY;

                postOnAnimation(view);

            } else {
                mFinished = false;
                mPrePosition = 0;
                this.view = null;
                Log.d(TAG, "run() scroll end");
            }
        }

        public boolean fling(View view, int startX, int startY, int velocityX, int velocityY,
                             int minX, int maxX, int minY, int maxY) {
            mFinished = false;
            mPrePosition = 0;
            mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            this.view = view;
            postOnAnimation(view);
            return false;
        }

        void postOnAnimation(View view) {
            view.removeCallbacks(this);
            if (!mFinished) {
                ViewCompat.postOnAnimation(view, this);
            }
        }
    }

    static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    PictureRecovyAnimateRunable mPictureRecovyAnimateRunable;

    //图片恢复动画
    class PictureRecovyAnimateRunable implements Runnable {
        String TAG = "RecovyAnimateRunable";
        private ScrollerCompat mScroller;
        View view;

        public PictureRecovyAnimateRunable(Context context) {
            mScroller = ScrollerCompat.create(context, sQuinticInterpolator);
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                int currY = mScroller.getCurrY();//向上  正  ;  向下  负
                int finalY = mScroller.getFinalY();

                Log.d(TAG, "run() scrolling currY:" + String.valueOf(currY) + " finalY:" + String.valueOf(finalY));

                ViewGroup viewGroup = (ViewGroup) view;
                View child0View = viewGroup.getChildAt(0);
             //   View child1View = viewGroup.getChildAt(1);

                child0View.getLayoutParams().height = currY;
                viewGroup.requestLayout();

                postOnAnimation(this.view);
            } else {
                this.view = null;

                Log.d(TAG, "run() scroll end");
            }
        }

        public void startScroll(View view, int startX, int startY, int dx, int dy) {
            mScroller.startScroll(startX, startY, dx, dy);
            this.view = view;
            postOnAnimation(view);
        }

        void postOnAnimation(View view) {
            view.removeCallbacks(this);
            ViewCompat.postOnAnimation(view, this);
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        Log.d(TAG, "onNestedFling()" + " velocityY:" + String.valueOf(velocityY) + " consumed:" + String.valueOf(consumed));

        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);

        // return true;
    }


    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        // return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        if (parent.getChildCount() != 2 && child instanceof RecyclerView){
            throw new IllegalStateException("the child~s count must is two and the second childer must is RecyclerView Type");
        }

        int parentHeight = parent.getMeasuredHeight();
        int parentWidth = parent.getMeasuredWidth();

     //   int child0Height = parent.getChildAt(0).getMeasuredHeight();

        int height = parentHeight/* - child0Height*/;

        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        int width = layoutParams.width;
        if (width == ViewGroup.LayoutParams.MATCH_PARENT) {
            width = parentWidth;
        }

        child.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        if (parent.getChildCount() != 2 && child instanceof RecyclerView){
            throw new IllegalStateException("the child~s count must is two and the second childer must is RecyclerView Type");
        }

        int child0Height = parent.getChildAt(0).getMeasuredHeight();
//        int h = parent.getChildAt(0).getLayoutParams().height;
//        if (h != child0Height) {
//            int x = 0;
//            x++;
//        }
        int child0Top = parent.getChildAt(0).getTop();
        int left = child.getLeft();
   //     int top = child.getTop();
        int width = child.getMeasuredWidth();
        int height = child.getMeasuredHeight();

        int realTop = child0Top + child0Height;
        child.layout(left, realTop, left + width, realTop + height);
        return true;//super.onLayoutChild(parent, child, layoutDirection);
    }
}
