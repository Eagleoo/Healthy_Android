package com.example.administrator.steps_count.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.model.Review;

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

public class ShowDynamic extends AppCompatActivity {
    private TextView show_dynamicname,show_curtime,show_describle,show_like_num,show_review_num;
    private ImageView show_circle_img,show_img_content;
    private EditText edt_review;
    private Button review_send;
    private Intent intent;
    private List<Review> ReviewList=new ArrayList<Review>();
    private List<Review> TempList=new ArrayList<Review>();
    private ListView review_list;
    private int review_num=0;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dynamic);
        show_dynamicname= (TextView) findViewById(R.id.show_dynamicname);
        show_curtime= (TextView) findViewById(R.id.show_curtime);
        show_describle= (TextView) findViewById(R.id.show_describle);
        show_like_num= (TextView) findViewById(R.id.show_like_num);
        show_review_num= (TextView) findViewById(R.id.show_review_num);
        show_circle_img=(ImageView)findViewById(R.id.show_circle_img);
        show_img_content=(ImageView)findViewById(R.id.show_img_content);
        edt_review=(EditText)findViewById(R.id.edt_review);
        review_send=(Button)findViewById(R.id.review_send);
        review_list=(ListView)findViewById(R.id.review_list);
        checkBox=(CheckBox)findViewById(R.id.like);
        checkBox.setClickable(false);
        intent=getIntent();
        review_num=Integer.valueOf(intent.getStringExtra("review_num"));
        show_dynamicname.setText(intent.getStringExtra("author"));
        show_curtime.setText(intent.getStringExtra("curtime"));
        show_describle.setText(intent.getStringExtra("content"));
        show_like_num.setText(intent.getStringExtra("like_num"));
        show_review_num.setText(intent.getStringExtra("review_num"));
        Glide.with(this).load(intent.getStringExtra("img")).into(show_circle_img);
        Glide.with(this).load(intent.getStringExtra("img_content")).into(show_img_content);
        if (intent.getStringExtra("islike").equals("yes")){
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }
        Load_Review();

        review_send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Frag_MainActivity.user.getUser_phone().equals("")){
                            Toast.makeText(ShowDynamic.this, "请先登录", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (edt_review.getText().toString().equals("")){
                                Toast.makeText(ShowDynamic.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Send_Review();
                            }
                        }
                    }
                }
        );

    }

    private void Load_Review(){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("function", "all")
                .add("action", intent.getStringExtra("id")).build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url("http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Dynamic")
                .post(requestBody)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ReviewList=getReview("review",json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                MyAdapter dynamic_adapter=new MyAdapter(ReviewList,ShowDynamic.this);
                                review_list.setAdapter(dynamic_adapter);
                            }
                        });
                }
            }
        });
    }

    private void Send_Review(){

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("function", "insertreview")
                .add("consult_id", intent.getStringExtra("id"))
                .add("portrait", Frag_MainActivity.user.getPortrait())
                .add("username", Frag_MainActivity.user.getUsername())
                .add("content", edt_review.getText().toString().trim()).build();

        final Request request = new Request.Builder()//创建Request 对象。
                .url("http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/Dynamic")
                .post(requestBody)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(ShowDynamic.this, "发表失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
//                    Looper.prepare();
//                    Toast.makeText(ShowDynamic.this, "发表成功", Toast.LENGTH_SHORT).show();
//                    Looper.loop();
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Review mall = new Review();
                                    SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
                                    if (TempList.size()<ReviewList.size()){
                                        TempList.addAll(ReviewList);
                                    }
                                    mall.setId(0);
                                    mall.setConsult_id(Integer.valueOf(intent.getStringExtra("id")));
                                    mall.setImag(sp.getString("portrait", ""));
                                    mall.setContent(edt_review.getText().toString().trim());
                                    mall.setUsername(Frag_MainActivity.user.getUsername());
                                    TempList.add(mall);
                                    review_num++;
                                    //show_review_num.setText(review_num);
                                    edt_review.setText("");
                                    MyAdapter dynamic_adapter=new MyAdapter(TempList,ShowDynamic.this);
                                    review_list.setAdapter(dynamic_adapter);
                                }
                            }
                    );


                }
            }
        });
    }



    private static List<Review> getReview(String key, String jsonString) throws JSONException {
        List<Review> list = new ArrayList<Review>();
        JSONArray jsonArray = new JSONArray(jsonString);
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                Review mall = new Review();
                mall.setId(jsonObject.getInt("id"));
                mall.setConsult_id(jsonObject.getInt("consult_id"));
                mall.setImag(jsonObject.getString("portrait"));
                mall.setContent(jsonObject.getString("content"));
                mall.setUsername(jsonObject.getString("username"));
                list.add(mall);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }


    public class MyAdapter extends BaseAdapter{
        public Context mContext;
        public List<Review> mList;

        public MyAdapter(List<Review> mList,Context mContext) {
            this.mList=mList;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mList.size();
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
        public View getView(int i, View view, ViewGroup parent) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.reviewlist, parent, false);
                holder = new ViewHolder();
                holder.imag = (ImageView) view.findViewById(R.id.reimag);
                holder.content = (TextView) view.findViewById(R.id.recontent);
                holder.name = (TextView) view.findViewById(R.id.reusername);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.name.setText(mList.get(i).getUsername());
            holder.content.setText(mList.get(i).getContent());
            Glide.with(ShowDynamic.this).load((mList.get(i).getImag())).placeholder( R.drawable.default_pic ).error( R.drawable.default_pic ).into(holder.imag);
//            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
//            ImageLoader.getInstance().init(configuration);
//            ImageLoader.getInstance().displayImage(mList.get(i).getImag(), holder.imag);
            return view;
        }

    }

    static class ViewHolder {
        ImageView imag;
        TextView name;
        TextView content;
    }

}
