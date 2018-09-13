package com.example.administrator.steps_count.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.CircleFragment;
import com.example.administrator.steps_count.model.Circle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.LinkedList;

/**
 * Created by Administrator on 2018/5/15.
 */

public class ConsultAdapter extends BaseAdapter {
private LinkedList<Circle> mdate;
private Context context;

    public ConsultAdapter(LinkedList<Circle> mdate, Context context) {
        this.mdate = mdate;
        this.context = context;
    }

    @Override
        public int getCount() {
            return mdate.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.consult_layout, viewGroup, false);
                holder=new ViewHolder();
                holder.img = (ImageView) view.findViewById(R.id.consultimg);
                holder.title = (TextView) view.findViewById(R.id.title);
                holder.eye_num = (TextView) view.findViewById(R.id.eye_num);
                holder.time = (TextView) view.findViewById(R.id.message_time);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.title.setText(mdate.get(i).getTitle());
            holder.eye_num.setText(mdate.get(i).getEye_num()+"");
            holder.time.setText(mdate.get(i).getTime());
            Glide.with(context).load((mdate.get(i).getImag())).placeholder( R.drawable.default_pic ).error( R.drawable.default_pic ).into(holder.img);
            return view;
        }

static class ViewHolder {
    ImageView img;
    TextView title;
    TextView eye_num;
    TextView time;
}
}
