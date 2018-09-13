package com.example.administrator.steps_count.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.tools.ScaleImageView;


public class LookActivity extends AppCompatActivity {

    private ScaleImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circleimage_look);

        iv=(ScaleImageView)findViewById(R.id.iv);
        Glide.with(this).load(getIntent().getStringExtra("imageUrl")).into(iv);
    }


}
