package cn.net.com.dragviews.imageupext2;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.net.com.dragviews.R;

public class ImageupExt2ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageup_ext2_list);

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(new MyAdapter());

        final ImageScaleViewGroup imageScaleViewGroup = (ImageScaleViewGroup) findViewById(R.id.imageScaleViewGroup);
        final TextView tvTable = (TextView) findViewById(R.id.tvTable);

        imageScaleViewGroup.setScaled(false);  //第一个视图是不是可以拉伸  比如图片视图


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
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 40;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, null);
            ViewGroup.LayoutParams layoutParams= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
            view.setLayoutParams(layoutParams);

            TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);
            tvInfo.setText(String.format("item %02d", position));
            return view;
        }
    }
}
