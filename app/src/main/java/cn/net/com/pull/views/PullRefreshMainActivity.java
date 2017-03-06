package cn.net.com.pull.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.net.com.dragviews.R;

public class PullRefreshMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_main);
    }

    public void onCliedForRecyclerView(View v) {
        startActivity(new Intent(this, PullRefreshRecyclerViewActivity.class));
    }

    public void onCliedForListView(View v) {
        startActivity(new Intent(this, PullRefreshListViewActivity.class));
    }

    public void onCliedForScrollView(View v) {
        startActivity(new Intent(this, PullRefreshScrollViewActivity.class));
    }

    public void onCliedForWebView(View v) {
        startActivity(new Intent(this, PullRefreshWebViewActivity.class));
    }

    public void onCliedForTextView(View v) {
        startActivity(new Intent(this, PullRefreshTextViewActivity.class));
    }
}
