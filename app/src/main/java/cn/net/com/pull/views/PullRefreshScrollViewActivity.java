package cn.net.com.pull.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.com.dragviews.R;
import cn.net.com.pull.base.PullRefreshViewGroup;

public class PullRefreshScrollViewActivity extends AppCompatActivity implements PullRefreshViewGroup.IRefreshLoad {
    LinearLayout linearLayoutContain;
    PullRefreshViewGroup pullRefreshViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_scroll_view);

        pullRefreshViewGroup = (PullRefreshViewGroup) findViewById(R.id.pullRefreshViewGroup);
        linearLayoutContain = (LinearLayout) findViewById(R.id.linearLayoutContain);

        pullRefreshViewGroup.setRefreshLoadListen(this);
        pullRefreshViewGroup.setHasPullRefresh(true);
        pullRefreshViewGroup.setHasLoadMore(true);

      //  addItem(20);
    }

    @Override
    public void pullRefreshStartLoad() {
        linearLayoutContain.postDelayed(new Runnable() {
            @Override
            public void run() {
                linearLayoutContain.removeAllViews();
                addItem(22);
                pullRefreshViewGroup.setPullRefreshLoadComplete();
            }
        }, 2000);
    }

    @Override
    public void pullUpStartLoadMore() {
        linearLayoutContain.postDelayed(new Runnable() {
            @Override
            public void run() {
                addItem(5);
                pullRefreshViewGroup.setPullUpLoadMoreComplete();
            }
        }, 2000);
    }

    void addItem(int count) {
        for (int i = 0; i < count; i++) {
            View view = LayoutInflater.from(linearLayoutContain.getContext()).inflate(R.layout.item_txt, null);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
            view.setLayoutParams(layoutParams);

            linearLayoutContain.addView(view);

            TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);
            tvInfo.setText(String.format("item %02d", linearLayoutContain.getChildCount()));
        }
    }
}
