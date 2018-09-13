package com.example.administrator.steps_count.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Address_adapter;
import com.example.administrator.steps_count.fragment.MallFragmen;
import com.example.administrator.steps_count.fragment.MeFragment;
import com.example.administrator.steps_count.step.Constant;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

/**
 * Created by PC on 2018/5/9.
 */

public class Mall_Address_Activity extends AppCompatActivity implements AdapterView.OnItemClickListener,Address_adapter.Address_function {
    private ListView mall_address_list;
    private List<Address> list=new ArrayList<Address>();
    private Button new_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_address_layout);
        mall_address_list= (ListView) findViewById(R.id.mall_address_list);
        new_address=(Button)findViewById(R.id.new_address);
        getAddr();
        mall_address_list.setOnItemClickListener(this);
        new_address.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Mall_Address_Activity.this,Addaddress_Acitvity.class));
                    }
                }
        );
        final RefreshLayout refreshLayout= (RefreshLayout)findViewById(R.id.addManager_refreshlayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshLayout.finishRefresh(2000);
                getAddr();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshLayout.finishRefresh(2000);
                getAddr();
            }
        });
    }

    private void getAddr() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", MeFragment.username).build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"selectaddr_Servlet")
                .post(requestBody)
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
                            list=getAddress("address",json);
                            Address_adapter address_adapter = new Address_adapter(getApplicationContext(),list);
                            mall_address_list.setAdapter(address_adapter);
                            address_adapter.setAddress_function(Mall_Address_Activity.this);
                        }
                    });
                }
            }
        });
    }

    private static List<Address> getAddress(String key, String jsonString) {
        List<Address> list = new ArrayList<Address>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Address address = new Address();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                address.setId(jsonObject2.getString("id"));
                address.setConsigneer(jsonObject2.getString("consigneer"));
                address.setCellnumber(jsonObject2.getString("cellnumber"));
                address.setAddress(jsonObject2.getString("address"));
                list.add(address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String consigneer=list.get(i).getConsigneer();
        String address=list.get(i).getAddress();
        String cellnumber=list.get(i).getCellnumber();
        Intent data=new Intent();
        data.putExtra("data1",consigneer);
        data.putExtra("data2",address);
        data.putExtra("data3",cellnumber);
        setResult(0x0003,data);
        finish();
    }

    @Override
    public void deleteaddr(String id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("id", id).build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Deleteaddr_Servlet")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("failure", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
//                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
