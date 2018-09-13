package com.example.administrator.steps_count.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Search_adapter;
import com.example.administrator.steps_count.fragment.MallFragmen;
import com.example.administrator.steps_count.step.Constant;

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
 * Created by PC on 2018/3/31.
 */

public class Mall_Search_Activity extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
    private ImageView mall_search_delete;
    private EditText mall_search_edit;
    private ListView mall_search_list;
    private Button mall_search_btn;
    private SearchView Search;
    private List<Mall_Name> mall_names_list=new ArrayList<Mall_Name>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_search_layout);
        init();
//        getMallName("");
//        Search_adapter search_adapter=new Search_adapter(getApplicationContext(),mall_names_list);
//        mall_search_list.setAdapter(search_adapter);
    }
    private void init()
    {
        mall_search_delete= (ImageView) findViewById(R.id.mall_search_delete);
        mall_search_edit= (EditText) findViewById(R.id.mall_search_edit);
        mall_search_list= (ListView) findViewById(R.id.mall_search_list);
        mall_search_btn= (Button) findViewById(R.id.mall_search_btn);
        mall_search_delete.setOnClickListener(this);
        mall_search_btn.setOnClickListener(this);
        mall_search_edit.addTextChangedListener(this);
        mall_search_list.setOnItemClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.mall_search_delete:
            //把EditText内容设置为空
            mall_search_edit.setText("");
            //把ListView隐藏
            mall_search_delete.setVisibility(View.GONE);
            break;
            case R.id.mall_search_btn:
                if(mall_search_edit.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"请输入搜索内容！",Toast.LENGTH_LONG).show();
                }
                else {
                    Bundle bundle=new Bundle();
                    bundle.putString("searchtext",mall_search_edit.getText().toString());
                    bundle.putString("check","mall");
                    Intent intent = new Intent(getApplicationContext(),Mall_Search_Finally_Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        if (charSequence.length() == 0) {
            //隐藏“删除”图片
            mall_search_delete.setVisibility(View.GONE);
        } else {//长度不为0
            //显示“删除图片”
            mall_search_delete.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        getMallName(String.valueOf(charSequence));
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.i("after","执行");
    }


    private void getMallName(String mall_name) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("search",mall_name)
                .add("type", "mall")
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Search_Servlet")
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
                            mall_names_list=getMall("mall_name",json);
                            Log.i("mall_name", String.valueOf(mall_names_list));
                            Search_adapter search_adapter=new Search_adapter(getApplicationContext(),mall_names_list);
                            mall_search_list.setAdapter(search_adapter);
                        }
                    });
                }
            }
        });
    }
    private static List<Mall_Name> getMall(String key, String jsonString) {
        List<Mall_Name> list = new ArrayList<Mall_Name>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);
            for (int i = 0; i < Persons.length(); i++) {
                Mall_Name mall_name = new Mall_Name();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                mall_name.setMall_name(jsonObject2.getString("mall_name"));
                list.add(mall_name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView textView= (TextView) view.findViewById(R.id.mall_search_item);
        mall_search_edit.setText(textView.getText());
    }
}
