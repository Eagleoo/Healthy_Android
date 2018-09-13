package com.example.administrator.steps_count.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.administrator.steps_count.Activity.CotentActivity;
import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.ConsultAdapter;
import com.example.administrator.steps_count.model.Circle;
import com.example.administrator.steps_count.model.Dynamics;
import com.example.administrator.steps_count.step.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Message_Frg extends Fragment {
    private LinkedList<Circle> circlelist = new LinkedList<Circle>();
    private ListView circlelistview;
    private Context context=getActivity();
    private ConsultAdapter consultAdapter;
    private SharedPreferences sp;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONArray jsonArray = new JSONArray(msg.obj.toString());
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

//                List<Circle> list = new LinkedList<Circle>();
//                if(circlelist.size()!=0){
//                    while (list.size()<8){
//                        int number = new Random().nextInt(circlelist.size()) + 0;
//                        if (!list.contains(circlelist.get(number))){
//                            list.add(circlelist.get(number));
//                        }
//                    }
//                }

                context = getActivity();
                if(context!=null){
                    consultAdapter = new ConsultAdapter(circlelist, context);
                    circlelistview.setAdapter(consultAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.message_frg, container, false);

        circlelistview = (ListView) view.findViewById(R.id.cusultlist);
        circlelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                insert_eys(circlelist.get(i).getId(),circlelist.get(i).getEye_num()+1);
                Intent intent = new Intent(getActivity(), CotentActivity.class);
                intent.putExtra("id", String.valueOf(circlelist.get(i).getId()));
                intent.putExtra("title", circlelist.get(i).getTitle());
                intent.putExtra("content", circlelist.get(i).getContent());
                intent.putExtra("img", circlelist.get(i).getImag());
                startActivity(intent);

            }
        });

        String url = "http://" + Frag_MainActivity.localhost + ":8080/circle/servlet/SelectMessage?type=circle&search=search";
        ReadURL(url);

        return view;
    }


    @SuppressLint("StaticFieldLeak")
    public void ReadURL(final String url) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int resultCode = connection.getResponseCode();
                    StringBuffer response = null;
                    if (HttpURLConnection.HTTP_OK == resultCode) {
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        response = new StringBuffer();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                    }

                    return response.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "1";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                Message message = new Message();
                message.obj = s;
                handler.sendMessage(message);
                super.onPostExecute(s);
            }


            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute(url);
    }

    public static void insert_eys(int id,int num) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("id",String.valueOf(id))
                .add("eye_num",String.valueOf(num))
                .build();
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Inser_eye_Servlet")
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
}
