package cn.net.com.pull.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.com.dragviews.R;
import cn.net.com.pull.base.PullRefreshViewGroup;

public class PullRefreshRecyclerViewActivity extends AppCompatActivity implements PullRefreshViewGroup.IRefreshLoad {
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    PullRefreshViewGroup pullRefreshViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_recycler_view);

        pullRefreshViewGroup = (PullRefreshViewGroup) findViewById(R.id.pullRefreshViewGroup);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        pullRefreshViewGroup.setRefreshLoadListen(this);
        pullRefreshViewGroup.setHasPullRefresh(true);
        pullRefreshViewGroup.setHasLoadMore(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    int mCount = 0;

    @Override
    public void pullRefreshStartLoad() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // if (mCount < 20) {
                mCount = 20;
                myAdapter.notifyDataSetChanged();
                // }
                pullRefreshViewGroup.setPullRefreshLoadComplete();
            }
        }, 2000);
    }

    @Override
    public void pullUpStartLoadMore() {
        recyclerView.postDelayed(new Runnable() {
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfo;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvInfo = (TextView) itemView.findViewById(R.id.tvInfo);
        }

        public void updateViews(int position) {
            tvInfo.setText(String.format("文本数据 %02d", (position + 1)));
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, null);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 100);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.updateViews(position);
        }

        @Override
        public int getItemCount() {
            return mCount;//0   2   14   20   40
        }
    }
}
