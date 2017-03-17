package cn.net.com.pull.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.net.com.dragviews.R;
import cn.net.com.pull.base.PullRefreshViewGroup;

public class PullRefreshListViewActivity extends AppCompatActivity implements PullRefreshViewGroup.IRefreshLoad {
    ListView listView;
    MyAdapter myAdapter;
    PullRefreshViewGroup pullRefreshViewGroup;

    List<String> urlList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_list_view);

        pullRefreshViewGroup = (PullRefreshViewGroup) findViewById(R.id.pullRefreshViewGroup);
        listView = (ListView) findViewById(R.id.listView);
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        pullRefreshViewGroup.setRefreshLoadListen(this);
        pullRefreshViewGroup.setHasPullRefresh(true);
        pullRefreshViewGroup.setHasLoadMore(true);
    }

    int mCount = 0;

    @Override
    public void pullRefreshStartLoad() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // if (mCount < 20) {
                mCount = 25;

                urlList.clear();
                for (int i=0; i<20; i++){
                    int size = urlList.size();
                    int index = size%PullRefreshRecyclerViewActivity.url.length;
                    urlList.add(PullRefreshRecyclerViewActivity.url[index]);
                }
                myAdapter.notifyDataSetChanged();
                // }
                pullRefreshViewGroup.setPullRefreshLoadComplete();
            }
        }, 2000);
    }

    @Override
    public void pullUpStartLoadMore() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //  if (mCount < 40) {
                mCount += 5;
                for (int i=0; i<5; i++){
                    int size = urlList.size();
                    int index = size%PullRefreshRecyclerViewActivity.url.length;
                    urlList.add(PullRefreshRecyclerViewActivity.url[index]);
                }
                myAdapter.notifyDataSetChanged();
                //  }
                pullRefreshViewGroup.setPullUpLoadMoreComplete();
            }
        }, 2000);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return urlList.size();//mCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);

//            TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);
//            tvInfo.setText(String.format("item %02d", position+1));

            ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            String url = urlList.get(position);
            Glide.with(parent.getContext())
                    .load(url)
                    .placeholder(R.mipmap.error)
                    .into(ivIcon);
            return view;
        }
    }
}
