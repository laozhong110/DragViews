package cn.net.com.pull.views;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.net.com.dragviews.R;
import cn.net.com.pull.base.PullRefreshViewGroup;

public class PullRefreshTextViewActivity extends AppCompatActivity implements PullRefreshViewGroup.IRefreshLoad{
    PullRefreshViewGroup pullRefreshViewGroup;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_text_view);

        pullRefreshViewGroup = (PullRefreshViewGroup) findViewById(R.id.pullRefreshViewGroup);

        textView = (TextView)findViewById(R.id.textView);

        pullRefreshViewGroup.setRefreshLoadListen(this);
        pullRefreshViewGroup.setHasLoadMore(false);
        pullRefreshViewGroup.setHasPullRefresh(true);
    }

    @Override
    public void pullRefreshStartLoad() {
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String f = simpleDateFormat.format(new Date());
                textView.setText(f);

                pullRefreshViewGroup.setPullRefreshLoadComplete();
            }
        },2000);
    }

    @Override
    public void pullUpStartLoadMore() {

    }
}
