package cn.net.com.dragviews;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;


public class MainActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;

    View addLongPressTip;

    MyFrameLayout myFrameLayout;

    TextView tvFirst,tvFirst_Second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Messenger messenger;

        Message msg;

                addLongPressTip = findViewById(R.id.addLongPressTip);

//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(
//                ObjectAnimator.ofFloat(addLongPressTip, "translationX", 0, 90),
//                ObjectAnimator.ofFloat(addLongPressTip, "translationX", 90, 0)
//        );
//        set.setDuration(5 * 1000).start();
//        set.setRepeatCount(ValueAnimator.INFINITE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(addLongPressTip, "translationY", 0, 30,0);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setDuration(1500);
        objectAnimator.start();

        tvFirst = (TextView)findViewById(R.id.tvFirst);
        tvFirst_Second = (TextView)findViewById(R.id.tvFirst_Second);

        myFrameLayout = (MyFrameLayout)findViewById(R.id.myFrameLayout);
        myFrameLayout.setOnSlideListen(new MyFrameLayout.OnSlideListen(){

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
    }


}
