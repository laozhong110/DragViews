package cn.net.com.dragviews.imageupext2;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import cn.net.com.dragviews.R;

public class ImageupExt2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageup_ext2);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MyAdapter());

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

        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"按钮被单击",Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvInfo;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvInfo = (TextView) itemView.findViewById(R.id.tvInfo);
        }

        public void updateViews(int position){
            tvInfo.setText(String.format("文本数据 %02d",position));
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt,null);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,100);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.updateViews(position);
        }

        @Override
        public int getItemCount() {
            return 14;//0   2   14   20   40
        }
    }
}
