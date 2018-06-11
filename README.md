# DragViews
Collection of animation frames
test

一，ImageScaleViewGroup 是一个可以拉伸顶部视图的控件(比如顶部的图片控件)  
支持RecyclerView  , Listview  ,ScrollView ,WebView  和其它的单视图控件



二，SlideViewGroup 是一个可以左右滑动的框架布局,模仿  "方片收集" 的主界面



三，PullRefreshViewGroup 是一个可以下拉刷新，上拉加载更多的控件，使用简单。 
支持RecyclerView  , Listview  ,ScrollView ,WebView  和其它的单视图控件
使用方法


layout.xml布局

	<cn.net.com.pull.base.PullRefreshViewGroup
        android:id="@+id/pullRefreshViewGroup"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#929292">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#2ee000" />
    </cn.net.com.pull.base.PullRefreshViewGroup>
    
    
 
 实现PullRefreshViewGroup.IRefreshLoad接口
 
 PullRefreshViewGroup pullRefreshViewGroup = (PullRefreshViewGroup) findViewById(R.id.pullRefreshViewGroup);   
 
 pullRefreshViewGroup.setRefreshLoadListen(this);
 
 pullRefreshViewGroup.setHasPullRefresh(true); //可以有下拉刷新 完后调用 pullRefreshViewGroup.setPullRefreshLoadComplete();
 
 pullRefreshViewGroup.setHasLoadMore(true);    //可以有上拉加载更多  【WebView和其它的单视图控件应该是不需要这个的】完成后调用 pullRefreshViewGroup.setPullUpLoadMoreComplete();
 
 
 问题 :ListView 还存在问题，尽在完善