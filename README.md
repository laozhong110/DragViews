# DragViews
Collection of animation frames
test

һ��ImageScaleViewGroup ��һ���������춥����ͼ�Ŀؼ�(���綥����ͼƬ�ؼ�)  
֧��RecyclerView  , Listview  ,ScrollView ,WebView  �������ĵ���ͼ�ؼ�



����SlideViewGroup ��һ���������һ����Ŀ�ܲ���,ģ��  "��Ƭ�ռ�" ��������



����PullRefreshViewGroup ��һ����������ˢ�£��������ظ���Ŀؼ���ʹ�ü򵥡� 
֧��RecyclerView  , Listview  ,ScrollView ,WebView  �������ĵ���ͼ�ؼ�
ʹ�÷���


layout.xml����

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
    
    
 
 ʵ��PullRefreshViewGroup.IRefreshLoad�ӿ�
 
 PullRefreshViewGroup pullRefreshViewGroup = (PullRefreshViewGroup) findViewById(R.id.pullRefreshViewGroup);   
 
 pullRefreshViewGroup.setRefreshLoadListen(this);
 
 pullRefreshViewGroup.setHasPullRefresh(true); //����������ˢ�� ������ pullRefreshViewGroup.setPullRefreshLoadComplete();
 
 pullRefreshViewGroup.setHasLoadMore(true);    //�������������ظ���  ��WebView�������ĵ���ͼ�ؼ�Ӧ���ǲ���Ҫ����ġ���ɺ���� pullRefreshViewGroup.setPullUpLoadMoreComplete();
 
 
 ���� :ListView ���������⣬��������