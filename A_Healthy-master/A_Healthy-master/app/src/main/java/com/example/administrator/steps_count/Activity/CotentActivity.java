package com.example.administrator.steps_count.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CotentActivity extends AppCompatActivity {
    private TextView title;
    private TextView content;
    private String id;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        title = (TextView) findViewById(R.id.title);
        img=(ImageView)findViewById(R.id.img_message);
        content = (TextView) findViewById(R.id.context);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        Glide.with(this).load(intent.getStringExtra("img")).placeholder( R.drawable.default_pic ).error( R.drawable.default_pic ).into(img);

    }
}
