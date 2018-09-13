package com.example.administrator.steps_count.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Mall_adapter;
import com.example.administrator.steps_count.mall.Banner_img;
import com.example.administrator.steps_count.mall.Mall;
import com.example.administrator.steps_count.mall.Mall_Detail_Activity;
import com.example.administrator.steps_count.mall.Mall_Search_Activity;
import com.example.administrator.steps_count.mall.User;
import com.example.administrator.steps_count.step.Constant;
import com.yanzhenjie.sofia.Sofia;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

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


public class MallFragmen extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, OnBannerListener {
    private Mall_adapter mall_adapter;
    private GridView gridView;
    private LinearLayout sort_layout;
    private List<Mall> MallList=new ArrayList<Mall>();
    private Button mall_btn1, mall_btn2, mall_btn3, mall_btn4, mall_btn5, mall_btn6;
    private RadioButton searchbutton;
    private Banner banner;
    private List<Banner_img> Banner_urls=new ArrayList<Banner_img>();
    private ArrayList titles;
    public static User user=new User();
    private RelativeLayout nowifi;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mall_layout, container, false);
        Sofia.with(getActivity())
                .statusBarBackground(ContextCompat.getColor(getActivity(), R.color.orange))
                .navigationBarBackground(ContextCompat.getDrawable(getActivity(), R.color.orange))
                .invasionStatusBar();
        mall_btn1 = (Button) view.findViewById(R.id.mall_btn1);
        mall_btn2 = (Button) view.findViewById(R.id.mall_btn2);
        mall_btn3 = (Button) view.findViewById(R.id.mall_btn3);
        mall_btn4 = (Button) view.findViewById(R.id.mall_btn4);
        mall_btn5 = (Button) view.findViewById(R.id.mall_btn5);
        mall_btn6 = (Button) view.findViewById(R.id.mall_btn6);
        searchbutton = (RadioButton) view.findViewById(R.id.mall_searchbtn);
        banner = (Banner) view.findViewById(R.id.mall_fragment_banner);
        //user.setUrl("http://192.168.1.111:8080/");
//        user.setUrl("http://192.168.1.104:8080/")
        mall_btn1.setOnClickListener(this);
        mall_btn2.setOnClickListener(this);
        mall_btn3.setOnClickListener(this);
        mall_btn4.setOnClickListener(this);
        mall_btn5.setOnClickListener(this);
        mall_btn6.setOnClickListener(this);
        searchbutton.setOnClickListener(this);

        gridView = (GridView) view.findViewById(R.id.mall_gridview);
        nowifi=(RelativeLayout) view.findViewById(R.id.nowifi);
        getDataBanner();
        getDataAsync();

        gridView.setOnItemClickListener(this);
        return view;
    }
    //分类按钮条点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mall_btn1:
                getDatabytype(mall_btn1.getText().toString());
                break;
            case R.id.mall_btn2:
                getDatabytype(mall_btn2.getText().toString());
                break;
            case R.id.mall_btn3:
                getDatabytype(mall_btn3.getText().toString());
                break;
            case R.id.mall_btn4:
                getDatabytype(mall_btn4.getText().toString());
                break;
            case R.id.mall_btn5:
                getDatabytype(mall_btn5.getText().toString());
                break;
            case R.id.mall_btn6:
                getDatabytype(mall_btn6.getText().toString());
                break;
            case R.id.mall_searchbtn:
                Intent intent1 = new Intent(getContext(), Mall_Search_Activity.class);
                startActivity(intent1);
                break;
        }
    }

    //商品点击事件处理
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String id=MallList.get(i).getMall_id();
        Bundle bundle=new Bundle();
        bundle.putString("0x0",id);
        Log.i("mall_id", id);
        Intent intent = new Intent(getContext(),Mall_Detail_Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //轮播图点击事件处理
    @Override
    public void OnBannerClick(int position) {
        switch (position) {
            case 0:
                Toast.makeText(getContext(), "0", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(getContext(), "1", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(getContext(), "2", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(getContext(), "3", Toast.LENGTH_LONG).show();
                break;
            case 4:
                Toast.makeText(getContext(), "4", Toast.LENGTH_LONG).show();
                break;
        }
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }

    }

    public void banner(List<String> list) {
        //设置banner点击事件
        banner.setOnBannerListener(this);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(list);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    //获取商品信息
    private void getDataAsync() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("action", "all").build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.CONNECTURL+"Login_Servlet")
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
                    final String json = response.body().string();
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Frag_MainActivity.dialog_cancle(getActivity());
                                MallList=getMall("mall",json);
                                mall_adapter = new Mall_adapter(getContext(),MallList);
                                gridView.setAdapter(mall_adapter);
                            }
                        });
                    }

                }
            }
        });
    }

    private static List<Mall> getMall(String key, String jsonString) {
        List<Mall> list = new ArrayList<Mall>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Mall mall = new Mall();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                mall.setMall_id(jsonObject2.getString("mall_id"));
                mall.setMall_name(jsonObject2.getString("mall_name"));
                mall.setMall_price(jsonObject2.getString("mall_price"));
                mall.setMall_img(Constant.CONNECTURL+jsonObject2.getString("mall_img"));
                list.add(mall);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    //获取banner信息
    private void getDataBanner() {
        Frag_MainActivity.dialog_show(getActivity());
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        final Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.CONNECTURL+"Banner_Servlet")
                .get()//传递请求体
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
                Log.i("failure", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Banner_urls=getBanner("banner",json);
                                if(Banner_urls.size()!=0){
                                    List<String> bannerlist=new ArrayList<>();
                                    bannerlist.add(Banner_urls.get(0).getBanner_img1());
                                    bannerlist.add(Banner_urls.get(0).getBanner_img2());
                                    bannerlist.add(Banner_urls.get(0).getBanner_img3());
                                    bannerlist.add(Banner_urls.get(0).getBanner_img4());
                                    bannerlist.add(Banner_urls.get(0).getBanner_img5());
                                    banner(bannerlist);
                                }

                            }
                        });
                    }

                }
            }
        });
    }

    private static List<Banner_img> getBanner(String key, String jsonString) {
        List<Banner_img> list = new ArrayList<Banner_img>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {

                Banner_img banner_img=new Banner_img();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                banner_img.setBanner_img1(Constant.CONNECTURL+jsonObject2.getString("banner_img1"));
                banner_img.setBanner_img2(Constant.CONNECTURL+jsonObject2.getString("banner_img2"));
                banner_img.setBanner_img3(Constant.CONNECTURL+jsonObject2.getString("banner_img3"));
                banner_img.setBanner_img4(Constant.CONNECTURL+jsonObject2.getString("banner_img4"));
                banner_img.setBanner_img5(Constant.CONNECTURL+jsonObject2.getString("banner_img5"));
                list.add(banner_img);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    private void getDatabytype(String type) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("action", type).build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"select_mallbytype_Servlet")
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
                    final String json = response.body().string();
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MallList=getMall("mall",json);
                                mall_adapter = new Mall_adapter(getContext(),MallList);
                                gridView.setAdapter(mall_adapter);
                            }
                        });
                    }

                }
            }
        });
    }

}
