package cn.net.com.dragviews.imageupext2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.net.com.dragviews.R;

public class ImageupExt2MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageup_ext2_main);
    }

    public void onCliedForRecyclerView(View v) {
        startActivity(new Intent(this, ImageupExt2Activity.class));
    }

    public void onCliedForListView(View v) {
        startActivity(new Intent(this, ImageupExt2ListActivity.class));
    }

    public void onCliedForScrollView(View v) {
        startActivity(new Intent(this, ImageupExt2ScrollViewActivity.class));
    }

    public void onCliedForWebView(View v) {
        startActivity(new Intent(this, ImageupExt2WebviewActivity.class));
    }

    public void onCliedForTextView(View v) {
        startActivity(new Intent(this, ImageupExt2TextActivity.class));
    }
}
