package cn.net.com.pull.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.net.com.dragviews.R;
import cn.net.com.pull.base.PullRefreshViewGroup;

public class PullRefreshListViewActivity extends AppCompatActivity implements PullRefreshViewGroup.IRefreshLoad {
    ListView listView;
    MyAdapter myAdapter;
    PullRefreshViewGroup pullRefreshViewGroup;

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
                myAdapter.notifyDataSetChanged();
                //  }
                pullRefreshViewGroup.setPullUpLoadMoreComplete();
            }
        }, 2000);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCount;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, null);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
            view.setLayoutParams(layoutParams);

            TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);
            tvInfo.setText(String.format("item %02d", position+1));
            return view;
        }
    }
}
