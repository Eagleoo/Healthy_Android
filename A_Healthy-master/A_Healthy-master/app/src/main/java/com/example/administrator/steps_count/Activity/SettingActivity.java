package com.example.administrator.steps_count.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.MeFragment;
import com.example.administrator.steps_count.mall.Addaddress_Acitvity;
import com.example.administrator.steps_count.mall.Mall_Address_Activity;
import com.example.administrator.steps_count.mall.Mall_Address_Manager_Activity;
import com.example.administrator.steps_count.model.Adress;
import com.google.gson.Gson;

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

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private RadioButton updatepwd;
    private RadioButton address;
    private RadioButton back;
    private RadioButton about;
    private ImageView fanhui;
    private Button exit;
    private AlertDialog.Builder builder=null;
    private AlertDialog alertDialog;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        gson=new Gson();
        updatepwd= (RadioButton) findViewById(R.id.pwdupdate);
        address= (RadioButton) findViewById(R.id.address);
        back= (RadioButton) findViewById(R.id.back);
        about= (RadioButton) findViewById(R.id.about);
        exit= (Button) findViewById(R.id.exit);
        fanhui= (ImageView) findViewById(R.id.fanhui);
        updatepwd.setOnClickListener(this);
        address.setOnClickListener(this);
        back.setOnClickListener(this);
        about.setOnClickListener(this);
        exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fanhui:
                finish();
                break;
            case R.id.pwdupdate:
                if (MeFragment.isLogin)
                {
                    Intent updatepwd=new Intent(SettingActivity.this,Updatepwd.class);
                    startActivity(updatepwd);
                }else {
                    Toast.makeText(SettingActivity.this,"请先登录！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.back:
               finish();
                break;
            case R.id.address:
                if(MeFragment.isLogin) {
                    Intent intent=new Intent(SettingActivity.this, Mall_Address_Manager_Activity.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(SettingActivity.this,"请先登录！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.about:
                Intent about=new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(about);
                break;
            case R.id.exit:
                if (!MeFragment.isLogin)
                {
                    Toast.makeText(SettingActivity.this,"请先登录",Toast.LENGTH_LONG).show();
                }else {
                    builder = new AlertDialog.Builder(SettingActivity.this);
                    alertDialog = builder.setIcon(R.drawable.pe)
                            .setMessage("确认退出?")
                            .setTitle("系统提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    Intent exit = new Intent();
                                    exit.setAction("android.intent.action.Broadcast");
                                    sendBroadcast(exit);
                                    Intent logout = new Intent(SettingActivity.this, LoginActivity.class);
                                    startActivity(logout);
                                }
                            })
                            .setNegativeButton("取消", null)

                            .create();
                    alertDialog.show();
                }
                break;


        }
    }


}
