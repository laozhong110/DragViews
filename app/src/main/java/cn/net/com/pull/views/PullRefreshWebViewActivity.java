package cn.net.com.pull.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.net.com.dragviews.R;
import cn.net.com.pull.base.PullRefreshViewGroup;

public class PullRefreshWebViewActivity extends AppCompatActivity implements PullRefreshViewGroup.IRefreshLoad {
    PullRefreshViewGroup pullRefreshViewGroup;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_web_view);

        pullRefreshViewGroup = (PullRefreshViewGroup) findViewById(R.id.pullRefreshViewGroup);
        pullRefreshViewGroup.setRefreshLoadListen(this);
        pullRefreshViewGroup.setHasPullRefresh(true);
        pullRefreshViewGroup.setHasLoadMore(false);

        webView = (WebView) findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //return super.shouldOverrideUrlLoading(view, url);

                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (pullRefreshViewGroup.isPullRefreshLoading()) {
                    pullRefreshViewGroup.setPullRefreshLoadComplete();
                }
            }
        });

        webView.loadUrl("http://www.baidu.com");
    }

    @Override
    public void pullRefreshStartLoad() {
        //  webView.reload();
        webView.loadUrl("http://cn.bing.com/");

//        webView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                pullRefreshViewGroup.setPullRefreshLoadComplete();
//            }
//        }, 2000);
    }

    @Override
    public void pullUpStartLoadMore() {

    }
}
