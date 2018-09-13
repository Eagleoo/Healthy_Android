package com.example.administrator.steps_count.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.administrator.steps_count.Activity.CotentActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.ConsultAdapter;
import com.example.administrator.steps_count.adapter.Mall_adapter;
import com.example.administrator.steps_count.fragment.MallFragmen;
import com.example.administrator.steps_count.fragment.Message_Frg;
import com.example.administrator.steps_count.model.Circle;
import com.example.administrator.steps_count.step.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Mall_Search_Finally_Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private GridView mall_search_finally_gridview;
    private ListView cusultlist;
    private List<Mall> mallList=new ArrayList<>();
    private LinkedList<Circle> circlelist = new LinkedList<Circle>();
    private String searchtext,check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_search_finally_layout);
        mall_search_finally_gridview= (GridView) findViewById(R.id.mall_search_finally_gridview);
        cusultlist= (ListView) findViewById(R.id.cusultlist);
        mall_search_finally_gridview.setOnItemClickListener(this);
        cusultlist.setOnItemClickListener(this);
        Bundle bundle=this.getIntent().getExtras();
        searchtext=bundle.getString("searchtext");
        check=bundle.getString("check");
        if (check.equals("main")){
            getMessageByName(searchtext);
        }
       else {
            getDataAsync();
        }

    }

    private void getMessageByName(String mall_name) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("search",mall_name)
                .add("type", "getByName")
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"circle/servlet/SelectMessage")
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(json);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = null;
                                    jsonObject = jsonArray.getJSONObject(i);
                                    Circle circle = new Circle();
                                    circle.setId(Integer.parseInt(jsonObject.get("id").toString()));
                                    circle.setTitle(jsonObject.get("title").toString());
                                    circle.setContent(jsonObject.get("content").toString());
                                    circle.setImag(jsonObject.get("imag").toString());
                                    circle.setEye_num(Integer.valueOf(jsonObject.get("eye_num").toString()));
                                    circle.setTime(jsonObject.get("time").toString());
                                    circlelist.add(circle);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ConsultAdapter mall_adapter = new ConsultAdapter(circlelist,Mall_Search_Finally_Activity.this);
                            cusultlist.setAdapter(mall_adapter);
                        }
                    });
                }
            }
        });
    }

    private void getDataAsync() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        RequestBody requestBody = new FormBody.Builder()
                .add("searchtext", searchtext).build();
        final Request request = new Request.Builder()//创建Request 对象。
                .url(Constant.CONNECTURL+"SelectSearchFinally_Servlet")
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mallList=getMall("mall",json);
                                Mall_adapter mall_adapter = new Mall_adapter(getApplicationContext(),mallList);
                                mall_search_finally_gridview.setAdapter(mall_adapter);
                            }
                        });
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (check.equals("main")){
            Message_Frg.insert_eys(circlelist.get(i).getId(),circlelist.get(i).getEye_num()+1);
            Intent intent = new Intent(Mall_Search_Finally_Activity.this, CotentActivity.class);
            intent.putExtra("id", String.valueOf(circlelist.get(i).getId()));
            intent.putExtra("title", circlelist.get(i).getTitle());
            intent.putExtra("content", circlelist.get(i).getContent());
            intent.putExtra("img", circlelist.get(i).getImag());
            startActivity(intent);
            finish();
        }
        else {
            String id=mallList.get(i).getMall_id();
            Bundle bundle=new Bundle();
            bundle.putString("0x0",id);
            Log.i("mall_id", id);
            Intent intent = new Intent(getApplicationContext(),Mall_Detail_Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

    }
}
