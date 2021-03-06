package cn.net.com.dragviews.imageupext2;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.net.com.dragviews.R;

public class ImageupExt2WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageup_ext2_webview);

        final ImageScaleViewGroup imageScaleViewGroup = (ImageScaleViewGroup) findViewById(R.id.imageScaleViewGroup);
        final TextView tvTable = (TextView) findViewById(R.id.tvTable);

        imageScaleViewGroup.setScaled(true);  //第一个视图是不是可以拉伸  比如图片视图


        tvTable.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvTable.getHeight() > 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tvTable.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }else{
                        tvTable.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    int nStationHeightUp = tvTable.getHeight();
                     imageScaleViewGroup.setStationHeightUp(nStationHeightUp); //这个高度只要不大于 ImageScaleViewGroup 的第一个子的高度就可以,也可以是0
                }
            }
        });

        WebView webView = (WebView) findViewById(R.id.webView);

        webView.setVerticalScrollBarEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //return super.shouldOverrideUrlLoading(view, url);

                view.loadUrl(url);

                return true;
            }
        });
        webView.loadUrl("http://www.baidu.com");
    }
}
