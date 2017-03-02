package cn.net.com.dragviews.imageupext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.com.dragviews.R;

public class ImageUpExtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_up_ext);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MyAdapter());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfo;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvInfo = (TextView) itemView.findViewById(R.id.tvInfo);
        }

        public void updateViews(int position) {
            tvInfo.setText(String.format("文本数据 %02d", position));
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, null);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 100);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.updateViews(position);
        }

        @Override
        public int getItemCount() {
            return 20;//0   2   14   20   40
        }
    }
}
