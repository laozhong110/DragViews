package cn.net.com.dragviews.entern;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.net.com.dragviews.MainActivity;
import cn.net.com.dragviews.R;
import cn.net.com.dragviews.imageup.ImageUpActivity;
import cn.net.com.dragviews.imageupext.ImageUpExtActivity;
import cn.net.com.dragviews.imageupext2.ImageupExt2MainActivity;
import cn.net.com.dragviews.viewgroup.ViewGroupActivity;

public class MainFrameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);
    }

    public void onCliedForFangpian1(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onCliedForFangpian2(View v) {
        startActivity(new Intent(this, ViewGroupActivity.class));
    }

    public void onCliedForTopPicture(View v) {
        startActivity(new Intent(this, ImageUpActivity.class));
    }

    public void onCliedForTopPicture2(View v) {
        startActivity(new Intent(this, ImageUpExtActivity.class));
    }

    public void onCliedForTopPicture3(View v) {
        startActivity(new Intent(this, ImageupExt2MainActivity.class));
    }
}
