package com.example.administrator.steps_count.mall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.MeFragment;
import com.example.administrator.steps_count.step.Constant;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by PC on 2018/3/24.
 */

public class Mall_Detail_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView mall_detail_shop;
    private TextView mall_detail_detail;
    private ImageView mall_detail_exit;
    private Button mall_detail_makeorder;
    private String mall_id;
    private CheckBox mall_detail_collect;
    private View line1;
    private View line2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_detail_layout);
        init();
        Bundle bundle;
        bundle = this.getIntent().getExtras();
        mall_id=bundle.getString("0x0");
        select_mallcollect_iscollect();
        dynamicFragment(new Mall_Shop_Fragment(),"mall_shop_fragment");

    }


    public void init()
    {
        mall_detail_detail= (TextView) findViewById(R.id.mall_detail_detail);
        mall_detail_shop= (TextView) findViewById(R.id.mall_detail_shop);
        line1=findViewById(R.id.mall_detail_line1);
        line2=findViewById(R.id.mall_detail_line2);
        mall_detail_exit= (ImageView) findViewById(R.id.mall_detail_exit);
        mall_detail_makeorder= (Button) findViewById(R.id.mall_detail_makeorder);
        mall_detail_collect= (CheckBox) findViewById(R.id.mall_detail_collect);
        mall_detail_detail.setOnClickListener(this);
        mall_detail_shop.setOnClickListener(this);
        mall_detail_exit.setOnClickListener(this);
        mall_detail_makeorder.setOnClickListener(this);
        mall_detail_collect.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.mall_detail_shop:
                refrshlinecolor();
                line1.setBackgroundColor(Color.BLACK);
                dynamicFragment(new Mall_Shop_Fragment(),"mall_shop_fragment");
                break;
            case R.id.mall_detail_detail:
                refrshlinecolor();
                line2.setBackgroundColor(Color.BLACK);
                dynamicFragment(new Mall_detail_Fragment(),"mall_shop_fragment");
                break;
            case R.id.mall_detail_exit:
                finish();
                break;
            case R.id.mall_detail_makeorder:
                if(!Frag_MainActivity.user.getUser_phone().equals("")){
                Intent intent=new Intent(getApplicationContext(),Mall_Firmorder_Acitvity.class);
                Bundle bundle=new Bundle();
                bundle.putString("0x3",mall_id);
                intent.putExtras(bundle);
                startActivity(intent);}
                else {
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mall_detail_collect:
                if(!Frag_MainActivity.user.getUser_phone().equals("")) {
                    if (mall_detail_collect.isChecked()) {
                        mall_detail_collect.setChecked(true);
                        insert_mallcollect();
                    } else {
                        mall_detail_collect.setChecked(false);
                        delete_mallcollect();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    mall_detail_collect.setChecked(false);
                }
                break;

        }
    }
    public void refrshlinecolor()
    {
        line1.setBackgroundColor(Color.parseColor("#00ffffff"));
        line2.setBackgroundColor(Color.parseColor("#00ffffff"));
    }

    public void dynamicFragment(android.support.v4.app.Fragment fragment,String tag){
        //1.获取碎片管理器
        FragmentManager supportFramentManager=getSupportFragmentManager();
        //2.开启一个事务
        FragmentTransaction beginTransaction=supportFramentManager.beginTransaction();
        //3.添加碎片
        beginTransaction.replace(R.id.mall_detail_framelayout,fragment,tag);
        //4.提交事务
        beginTransaction.commit();
    }

    private void insert_mallcollect() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username",MeFragment.username)
                .add("mall_id",mall_id)
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Inser_mallcollect_Servlet")
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),issuccess,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void delete_mallcollect() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username",MeFragment.username)
                .add("mall_id",mall_id)
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Delete_mallcollect_Servlet")
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),issuccess,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void select_mallcollect_iscollect() {
        if(Frag_MainActivity.user!=null){
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username",MeFragment.username)
                    .add("mall_id",mall_id)
                    .build();
            final Request request = new Request.Builder()
                    .url(Constant.CONNECTURL+"Select_mallcollect_iscollect_Servlet")
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
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (issuccess.equals("yes"))
                                                                {
                                                                    mall_detail_collect.setChecked(true);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }

                                            }
            );
        }


    }


}
