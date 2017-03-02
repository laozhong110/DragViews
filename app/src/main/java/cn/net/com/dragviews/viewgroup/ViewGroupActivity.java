package cn.net.com.dragviews.viewgroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import cn.net.com.dragviews.MyFrameLayout;
import cn.net.com.dragviews.R;
import cn.net.com.dragviews.viewgroup.control.SlideViewGroup;

public class ViewGroupActivity extends AppCompatActivity {
    View addLongPressTip;

    TextView tvFirst,tvFirst_Second;

    SlideViewGroup slideViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        addLongPressTip = findViewById(R.id.addLongPressTip);

        slideViewGroup = (SlideViewGroup)findViewById(R.id.slideViewGroup);
        slideViewGroup.setLeftWidth(300);
        slideViewGroup.setMaxOffset(100);
        slideViewGroup.setTouchWidth(160);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(addLongPressTip, "translationY", 0, 30,0);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setDuration(1500);
        objectAnimator.start();

        tvFirst = (TextView)findViewById(R.id.tvFirst);
        tvFirst_Second = (TextView)findViewById(R.id.tvFirst_Second);

        slideViewGroup.setOnSlideListen(new SlideViewGroup.OnSlideListen() {
            @Override
            public void onSlideFinish(int nDockType) {
                if (nDockType == MyFrameLayout.DOCK_RIGHT){
                    tvFirst.setText("管理分类");
                    tvFirst_Second.setText("分类改名/删除");
                }else{
                    tvFirst.setText("全部");
                    tvFirst_Second.setText("1条");
                }
            }
        });

        findViewById(R.id.btnClearRecycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"你单击了清空按钮",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnLongTest).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"你长按了卡片",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        findViewById(R.id.btnAddInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"你单击了添加按钮",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnAddInfo).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"你长按了添加按钮",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}

