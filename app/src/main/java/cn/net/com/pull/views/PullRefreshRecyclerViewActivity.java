package cn.net.com.pull.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import cn.net.com.dragviews.R;
import cn.net.com.pull.base.PullRefreshViewGroup;

public class PullRefreshRecyclerViewActivity extends AppCompatActivity implements PullRefreshViewGroup.IRefreshLoad {
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    PullRefreshViewGroup pullRefreshViewGroup;

    List<String> urlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_recycler_view);

        pullRefreshViewGroup = (PullRefreshViewGroup) findViewById(R.id.pullRefreshViewGroup);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        pullRefreshViewGroup.setRefreshLoadListen(this);
        pullRefreshViewGroup.setHasPullRefresh(true);
        pullRefreshViewGroup.setHasLoadMore(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    int mCount = 0;

    @Override
    public void pullRefreshStartLoad() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // if (mCount < 20) {
              //  mCount = 20;
                urlList.clear();
                for (int i=0; i<20; i++){
                    int size = urlList.size();
                    int index = size%url.length;
                    urlList.add(url[index]);
                }
                myAdapter.notifyDataSetChanged();
                // }
                pullRefreshViewGroup.setPullRefreshLoadComplete();
            }
        }, 2000);
    }

    @Override
    public void pullUpStartLoadMore() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //  if (mCount < 40) {
              //  mCount += 5;
                for (int i=0; i<5; i++){
                    int size = urlList.size();
                    int index = size%url.length;
                    urlList.add(url[index]);
                }
                myAdapter.notifyDataSetChanged();
                //  }
                pullRefreshViewGroup.setPullUpLoadMoreComplete();
            }
        }, 2000);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvInfo;
        Context mContext;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvInfo = (TextView) itemView.findViewById(R.id.tvInfo);

            mContext = ivIcon.getContext();
        }

        public void updateViews(int position) {
            //tvInfo.setText(String.format("文本数据 %02d", (position + 1)));

            String url = urlList.get(position);
//            Picasso.with(ivIcon.getContext().getApplicationContext())
//                    .load(url)
//                   // .transform(new RoundedCornersTransformation(10, 10))
//                    .placeholder(R.mipmap.error)
//                    .error(R.mipmap.error)
//                    .fit()
//                    .into(ivIcon,new com.squareup.picasso.Callback(){
//
//                        @Override
//                        public void onSuccess() {
//                            int x = 0;
//                            x++;
//                        }
//
//                        @Override
//                        public void onError() {
//                            int x = 0;
//                            x++;
//                        }
//                    });

            Glide.with(mContext)
                    .load(url)
                    //.bitmapTransform(new CropTransformation(ivIcon.getContext()))
                   // .bitmapTransform(new RoundedCornersTransformation(mContext, 60, 0, RoundedCornersTransformation.CornerType.ALL))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(ivIcon);
            tvInfo.setText(urlList.get(position));
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_txt, null);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.updateViews(position);
        }

        @Override
        public int getItemCount() {
            return urlList.size();//0   2   14   20   40
        }
    }


    public static String []url = {
            "http://tse3.mm.bing.net/th?id=OIP.9QywTC1QlqlEZGjpCdlM_gEsDH&w=198&h=131&c=7&qlt=90&o=4&pid=1.7",
            "http://tse2.mm.bing.net/th?id=OIP.jwNppEitv6hzxZCLZ0N-owEsDH&w=198&h=131&c=7&qlt=90&o=4&pid=1.7",
            "http://tse4.mm.bing.net/th?id=OIP.MRDrzPpFaqH5VpDmo6ojFQDUEs&w=198&h=280&c=7&qlt=90&o=4&pid=1.7",
            "http://tse4.mm.bing.net/th?id=OIP.d5CxMtOHWNuImxU7nWspYQDIEs&w=198&h=297&c=7&qlt=90&o=4&pid=1.7",
            "http://tse4.mm.bing.net/th?id=OIP.d5CxMtOHWNuImxU7nWspYQDIEs&w=198&h=297&c=7&qlt=90&o=4&pid=1.7",
            "http://tse2.mm.bing.net/th?id=OIP.TodHR1vMUYm-lRECvPchtQDVEs&w=198&h=278&c=7&qlt=90&o=4&pid=1.7",
            "http://pic1.win4000.com/pic/e/9d/9482461577.jpg",
            "http://tse1.mm.bing.net/th?id=OIP.pXQe1laQEdqradU4FtCcmgDIEs&w=198&h=297&c=7&qlt=90&o=4&pid=1.7",
            "http://tse4.mm.bing.net/th?id=OIP.B8D7NsoMb4VQZVuHo70oLQDIEs&w=198&h=297&c=7&qlt=90&o=4&pid=1.7",
            "http://tse4.mm.bing.net/th?id=OIP.0Q50pF-pOtYsQfZV4q2mtADIEs&w=198&h=297&c=7&qlt=90&o=4&pid=1.7"
    };
}
