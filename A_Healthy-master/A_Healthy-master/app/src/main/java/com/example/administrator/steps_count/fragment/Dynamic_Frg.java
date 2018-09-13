package com.example.administrator.steps_count.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.Activity.ShowDynamic;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Dynamics;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.tools.StaggeredGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Dynamic_Frg extends Fragment {
    private List<Dynamics> MallList=new ArrayList<Dynamics>();
    private  StaggeredGridView gridView;
    private SharedPreferences sp;
    private String islike="";//是否点赞
    private Handler handler1=null;
    private RelativeLayout nowifi;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dynamic_frg, container, false);
        gridView = (StaggeredGridView) view.findViewById(R.id.staggeredGridView);
        nowifi=(RelativeLayout) view.findViewById(R.id.nowifi);

        int margin = getResources().getDimensionPixelSize(R.dimen.margin);
        gridView.setItemMargin(margin); // set the GridView margin
        gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well
        sp=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        //创建属于主线程的handler
        handler1=new Handler();
        gridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {

            @Override
            public void onItemClick(StaggeredGridView parent, View v, int i,
                                    long id) {
                CheckBox checkBox=(CheckBox)v.findViewById(R.id.like1);
                Intent intent=new Intent(getActivity(), ShowDynamic.class);
                if (checkBox.isChecked()){
                    intent.putExtra("islike", "yes");
                }
                else {
                    intent.putExtra("islike", "no");
                }

                intent.putExtra("id",MallList.get(i).getId());
                intent.putExtra("author",MallList.get(i).getAuthor());
                intent.putExtra("curtime",MallList.get(i).getTime());
                intent.putExtra("content",MallList.get(i).getContent());
                intent.putExtra("id",String.valueOf(MallList.get(i).getId()));
                intent.putExtra("img",MallList.get(i).getImg());
                intent.putExtra("img_content",MallList.get(i).getImg_content());
                intent.putExtra("like_num",String.valueOf(MallList.get(i).getLike_num()));
                intent.putExtra("review_num",String.valueOf(MallList.get(i).getReview_num()));
                startActivity(intent);
            }
        });
        Load_Dynamic();

        return view;
    }


    static  class ViewHolder
    {
        ImageView circle_img;
        TextView author;
        TextView curtime;
        TextView content;
        ImageView img_content;
        TextView like_num;
        TextView review_num;
        TextView dynamicLocation;
        CheckBox like_img;
    }

    private void Load_Dynamic(){
        Frag_MainActivity.dialog_show(getActivity());
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "all").build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url("http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Show_Dynamic")
                .post(requestBody)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Frag_MainActivity.dialog_cancle(getActivity());
                if (getActivity()!=null){
                    getActivity().runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    nowifi.setVisibility(View.VISIBLE);
                                }
                            }
                    );
                }

                Log.e("failure", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Frag_MainActivity.dialog_cancle(getActivity());
                                MallList=getMall("mall",json);
                                Dynamic_Adapter dynamic_adapter=new Dynamic_Adapter(MallList,getContext());
                                gridView.setAdapter(dynamic_adapter);
                                dynamic_adapter.notifyDataSetChanged();
                            }
                        });
                    }

                }
            }
        });
    }

    private static List<Dynamics> getMall(String key, String jsonString) {
        List<Dynamics> list = new ArrayList<Dynamics>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                Dynamics dynamics=new Dynamics();
                dynamics.setId(Integer.parseInt(jsonObject.get("id").toString()));
                dynamics.setImg(jsonObject.get("img").toString());
                dynamics.setImg_content(jsonObject.get("img_content").toString());
                dynamics.setContent(jsonObject.get("content").toString());
                dynamics.setAuthor(jsonObject.get("author").toString());
                dynamics.setTime(jsonObject.get("time").toString());
                dynamics.setDynamicLocation(jsonObject.get("dynamicLocation").toString());
                dynamics.setLike_num(Integer.valueOf(jsonObject.get("like_num").toString()));
                dynamics.setReview_num(Integer.valueOf(jsonObject.get("review_num").toString()));
                list.add(dynamics);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public class Dynamic_Adapter extends BaseAdapter {
        public Context mContext;
        public List<Dynamics> mList;
        public Dynamic_Adapter(List<Dynamics> mList, Context mContext) {
            this.mList=mList;
            this.mContext = mContext;
        }
        @Override
        public int getCount() {
            return  mList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder=null;
            if(view==null)
            {
                view=LayoutInflater.from(mContext).inflate(R.layout.circle_item,viewGroup,false);
                viewHolder=new ViewHolder();
                viewHolder.author= (TextView) view.findViewById(R.id.dynamicname);
                viewHolder.curtime= (TextView) view.findViewById(R.id.curtime);
                viewHolder.content= (TextView) view.findViewById(R.id.describle);
                viewHolder.dynamicLocation= (TextView) view.findViewById(R.id.dynamicLocation);
                viewHolder.like_num= (TextView) view.findViewById(R.id.like_num1);
                viewHolder.review_num= (TextView) view.findViewById(R.id.review_num);
                viewHolder.circle_img=(ImageView)view.findViewById(R.id.circle_img) ;
                viewHolder.img_content=(ImageView)view.findViewById(R.id.imageView1) ;
                viewHolder.like_img=(CheckBox) view.findViewById(R.id.like1) ;
                view.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            select_islike(mList.get(i).getId(),viewHolder.like_img);
            viewHolder.author.setText(mList.get(i).getAuthor());
            viewHolder.curtime.setText(mList.get(i).getTime());
            viewHolder.content.setText(mList.get(i).getContent());
            viewHolder.like_num.setText(String.valueOf(mList.get(i).getLike_num()));
            viewHolder.review_num.setText(String.valueOf(mList.get(i).getReview_num()));
            viewHolder.dynamicLocation.setText(mList.get(i).getDynamicLocation());
            Glide.with(getActivity()).load((mList.get(i).getImg_content())).placeholder( R.drawable.default_pic ).error( R.drawable.default_pic ).into(viewHolder.img_content);
            Glide.with(getActivity()).load(mList.get(i).getImg()).placeholder( R.drawable.default_pic ).error( R.drawable.default_pic ).into(viewHolder.circle_img);
            final ViewHolder holder = viewHolder;
            viewHolder.like_img.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (holder.like_img.isChecked()){
                                int num=Integer.valueOf(holder.like_num.getText().toString());
                                num++;
                                holder.like_num.setText(num+"");
                                insert_like(mList.get(i).getId());
                            }
                            else {
                                int num=Integer.valueOf(holder.like_num.getText().toString());
                                num--;
                                holder.like_num.setText(num+"");
                                delete_like(mList.get(i).getId());
                            }
                        }
                    }
            );

            return view;
        }
    }

    private void insert_like(int id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username",sp.getString("username", ""))
                .add("consult_id",String.valueOf(id))
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Inser_like_Servlet")
                .post(requestBody)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("failure", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String issuccess = response.body().string();
                    Log.e("点赞返回","返回"+issuccess);
                }
            }
        });
    }
    private void delete_like(int id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username",sp.getString("username", ""))
                .add("consult_id",String.valueOf(id))
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Delete_insert_Servlet")
                .post(requestBody)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("failure", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String issuccess = response.body().string();
                    Log.e("删除返回","返回"+issuccess);
                }
            }
        });
    }
    private void select_islike(int id, final CheckBox checkBox) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username",sp.getString("username", ""))
                .add("consult_id",String.valueOf(id))
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Select_islike_Servlet")
                .post(requestBody)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Log.i("failure", "onFailure: ");
                                            }
                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                if (response.isSuccessful()) {
                                                    islike = response.body().string();
                                                    if (islike.equals("yes")){  // 构建Runnable对象，在runnable中更新界面
                                                        final Runnable runnableUi=new Runnable(){
                                                            @Override
                                                            public void run() {
                                                                //更新界面
                                                                checkBox.setChecked(true);
                                                            }

                                                        };
                                                        new Thread(
                                                                new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        handler1.post(runnableUi);
                                                                    }
                                                                }
                                                        ).start();
                                                    }
                                                }
                                            }

                                        }
        );

    }
}
