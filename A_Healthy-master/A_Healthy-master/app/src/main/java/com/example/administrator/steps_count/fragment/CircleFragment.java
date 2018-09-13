package com.example.administrator.steps_count.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.steps_count.Activity.Publishdy;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.mall.MyFragmentPagerAdapter;
import com.yanzhenjie.sofia.Sofia;

import java.util.ArrayList;
import java.util.List;

public class CircleFragment extends Fragment implements View.OnClickListener{
    private ViewPager pager;
    private TextView tv_dynamic;
    private TextView tv_message;
    private int pagecount =0;
    private List<Fragment> fragList;
    private ImageView camera;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null) {
//                parent.removeView(view);
//            }
//            return view;
//        }
        Sofia.with(getActivity())
                .statusBarBackground(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .navigationBarBackground(ContextCompat.getDrawable(getActivity(), R.color.colorPrimary))
                .invasionStatusBar();
        view = inflater.inflate(R.layout.circle_layout, container, false);
        pager=(ViewPager) view.findViewById(R.id.dy_viewpager);
        tv_dynamic=(TextView)view.findViewById(R.id.tv_dynamic);
        tv_message=(TextView)view.findViewById(R.id.tv_message);
        camera=(ImageView)view.findViewById(R.id.camera);
        initview();
        return view;
    }

    public void initview()
    {


        tv_message.setOnClickListener(this);
        tv_dynamic.setOnClickListener(this);

        fragList=new ArrayList<Fragment>();
        fragList.add(new Dynamic_Frg());
        fragList.add(new Message_Frg());

        MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getChildFragmentManager(), fragList);

        pager.setAdapter(adapter);

        pager.setCurrentItem(pagecount);

        camera.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), Publishdy.class));
                    }
                }
        );

        pager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (position==0){
                            tv_dynamic.setTextSize(25);
                            tv_message.setTextSize(20);
                        }
                        else {
                            tv_message.setTextSize(25);
                            tv_dynamic.setTextSize(20);
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
                        changeTabColor(position);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dynamic:
                pager.setCurrentItem(0);
                tv_dynamic.setTextColor(Color.BLACK);
                tv_dynamic.setTextSize(30);
                tv_message.setTextSize(18);
                break;
            case R.id.tv_message:
                pager.setCurrentItem(1);
                tv_message.setTextColor(Color.BLACK);
                tv_message.setTextSize(30);
                tv_dynamic.setTextSize(18);
                break;
        }
    }

    private void changeTabColor(int position){
        if(0 == position) {
            tv_dynamic.setTextColor(Color.parseColor("#000000"));
        }

        if(1== position) {
            tv_message.setTextColor(Color.parseColor("#000000"));
        }
    }

}


