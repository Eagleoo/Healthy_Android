package com.example.administrator.steps_count.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.example.administrator.steps_count.Activity.Frag_MainActivity;
import com.example.administrator.steps_count.Activity.LoginActivity;
import com.example.administrator.steps_count.Main_Activity.Drink_Coffee_Activity;
import com.example.administrator.steps_count.Main_Activity.Eat_Activity;
import com.example.administrator.steps_count.Main_Activity.Text_Activity;
import com.example.administrator.steps_count.Main_Activity.User_Data;
import com.example.administrator.steps_count.Main_Activity.Weight_Activity;
import com.example.administrator.steps_count.Main_Activity.Weight_Add_Activity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.adapter.Search_adapter;
import com.example.administrator.steps_count.mall.Mall_Name;
import com.example.administrator.steps_count.mall.Mall_Search_Finally_Activity;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.DBOpenHelper;
import com.example.administrator.steps_count.step.Step_Map_Activity;
import com.example.administrator.steps_count.step.TimeUtil;
import com.example.administrator.steps_count.step.User_DBOpenHelper;
import com.example.administrator.steps_count.tools.Json_Tools;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yanzhenjie.sofia.Sofia;

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

public class MainFragment extends Fragment implements View.OnClickListener,TextWatcher,AdapterView.OnItemClickListener{
    private GridLayout grid_layout;
    private ViewAnimator viewAnimator,animator_text;
    private final long TIME_INTERVAL = 4000L;
    private RollPagerView mRollViewPager;
    private boolean autoPlayFlag = true;
    private TextView txt_timeCount,txt_min,txt_plan,tv_weight,tv_cur_ka,drink_num,coffee_num,img_explore;
    private ScrollView sc;
    private String string;
    private Button btn_weight,btn_eat,btn_step_start,btn_run_start;
    private LinearLayout linear_weight,linear_eat,linear_run,linear_step,linear_drink,linear_coffee;
    private DBOpenHelper db;
    private User_DBOpenHelper u_db;
    private ImageView add_drink,add_coffee,drink_,coffee_;
    private Context context;
    private ListView search_list;
    private EditText edt_explore;
    private List<Mall_Name> mall_names_list=new ArrayList<Mall_Name>();
    private Search_adapter search_adapter;
    private RelativeLayout nowifi;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {

            if(autoPlayFlag){
                showNext();
            }
            else {
                super.handleMessage(msg);
            }
            handler.sendMessageDelayed(new Message(),TIME_INTERVAL);
        }
    };

    @Override
    public void onAttach(Context context) {
        context=getActivity();
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_layout, container, false);
        Sofia.with(getActivity())
                .statusBarBackground(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .navigationBarBackground(ContextCompat.getDrawable(getActivity(), R.color.colorPrimary))
                .invasionStatusBar();

        sc=(ScrollView)view.findViewById(R.id.sc);
        edt_explore=(EditText)view.findViewById(R.id.edt_explore);
        img_explore=(TextView) view.findViewById(R.id.img_explore);
        search_list=(ListView)view.findViewById(R.id.search_list);
        txt_timeCount=(TextView)view.findViewById(R.id.txt_timeCount);
        txt_min=(TextView)view.findViewById(R.id.txt_min);
        txt_plan=(TextView)view.findViewById(R.id.txt_plan);
        tv_weight=(TextView)view.findViewById(R.id.tv_weight);
        tv_cur_ka=(TextView)view.findViewById(R.id.tv_cur_ka);
        btn_weight=(Button)view.findViewById(R.id.btn_weight);
        btn_eat=(Button)view.findViewById(R.id.btn_eat);
        btn_step_start=(Button)view.findViewById(R.id.btn_step_start);
        btn_run_start=(Button)view.findViewById(R.id.btn_run_start);
        linear_weight=(LinearLayout)view.findViewById(R.id.linear_weight);
        linear_eat=(LinearLayout)view.findViewById(R.id.linear_eat);
        linear_step=(LinearLayout)view.findViewById(R.id.linear_step);
        linear_run=(LinearLayout)view.findViewById(R.id.linear_run);
        linear_drink=(LinearLayout)view.findViewById(R.id.linear_drink);
        linear_coffee=(LinearLayout)view.findViewById(R.id.linear_coffee);
        add_drink=(ImageView)view.findViewById(R.id.add_drink);
        add_coffee=(ImageView)view.findViewById(R.id.add_coffee);
        drink_=(ImageView)view.findViewById(R.id.drink_);
        coffee_=(ImageView)view.findViewById(R.id.coffee_);
        drink_num=(TextView)view.findViewById(R.id.drink_num);
        coffee_num=(TextView)view.findViewById(R.id.coffee_num);

        viewAnimator=(ViewAnimator)view.findViewById(R.id.animator) ;
        animator_text=(ViewAnimator)view.findViewById(R.id.animator_text) ;
        mRollViewPager = (RollPagerView)view.findViewById(R.id.roll_view_pager);
        grid_layout=(GridLayout)view.findViewById(R.id.grid_layout);
        nowifi=(RelativeLayout) view.findViewById(R.id.nowifi);;
        db=new DBOpenHelper(getActivity());
        u_db=new User_DBOpenHelper(getActivity());

        if(u_db.getCurUserDateByDate(TimeUtil.getCurrentDate())==null){
            init();
        }
        else {
            tv_cur_ka.setText(u_db.getCurUserDateByDate(TimeUtil.getCurrentDate()).getUser_ka());
            drink_num.setText(u_db.getCurUserDateByDate(TimeUtil.getCurrentDate()).getUser_drink());
            coffee_num.setText(u_db.getCurUserDateByDate(TimeUtil.getCurrentDate()).getUser_coffee());
        }

        if(db.getWeight()==null){
            tv_weight.setText("0.00公斤");
        }
        else {
            tv_weight.setText(db.getWeight()+"公斤");
        }

        handler.sendMessageDelayed(new Message(),TIME_INTERVAL);

        //设置播放时间间隔
        mRollViewPager.setPlayDelay(4000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(new TestNormalAdapter());

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        mRollViewPager.setHintView(new ColorPointHintView(getActivity(), Color.YELLOW,Color.WHITE));
        sale_TimeCount();
        Plan_Count();
        txt_plan.setText(string);

        onClickInit();
        return view;
    }

    private void sale_TimeCount(){
        TimeCount time = new TimeCount(60000, 1000);
        time.start();
    }


    public void showNext(){

        if(getActivity()!=null){
            viewAnimator.setOutAnimation(getActivity(),R.anim.slide_out_up);
            viewAnimator.setInAnimation(getActivity(),R.anim.slide_in_down);
            animator_text.setOutAnimation(getActivity(),R.anim.slide_out_up);
            animator_text.setInAnimation(getActivity(),R.anim.slide_in_down);
            animator_text.showNext();
            viewAnimator.showNext();
        }

    }

    private void onClickInit(){
        viewAnimator.setOnClickListener(this);
        animator_text.setOnClickListener(this);
        btn_step_start.setOnClickListener(this);
        btn_run_start.setOnClickListener(this);
        linear_step.setOnClickListener(this);
        linear_run.setOnClickListener(this);
        add_coffee.setOnClickListener(this);
        add_drink.setOnClickListener(this);
        btn_eat.setOnClickListener(this);
        linear_eat.setOnClickListener(this);
        linear_weight.setOnClickListener(this);
        btn_weight.setOnClickListener(this);
        grid_layout.setOnClickListener(this);
        drink_.setOnClickListener(this);
        coffee_.setOnClickListener(this);
        linear_drink.setOnClickListener(this);
        linear_coffee.setOnClickListener(this);
        edt_explore.addTextChangedListener(this);
        search_list.setOnItemClickListener(this);
        img_explore.setOnClickListener(this);
        nowifi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        switch (v.getId()) {

            case R.id.animator_text: startActivity(new Intent(getActivity(), Text_Activity.class));break;

            case R.id.animator:
                RadioButton radioButton=(RadioButton)getActivity().findViewById(R.id.tv_circle);
                RadioButton radioButton3=(RadioButton)getActivity().findViewById(R.id.tv_main);
                radioButton.setChecked(true);
                radioButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                radioButton3.setTextColor(getResources().getColor(R.color.gray_default_dark));
                Fragment fragment=new Message_Frg();
                fm.beginTransaction().replace(R.id.framelayout,fragment).commit();break;

            case R.id.btn_step_start:
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(getActivity(),LoginActivity.class));break;
                }
                else {
                    startActivity(new Intent(getActivity(), Step_Map_Activity.class));break;
                }


            case R.id.btn_run_start:
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(getActivity(),LoginActivity.class));break;
                }
                else {
                    startActivity(new Intent(getActivity(), Step_Map_Activity.class));break;
                }

            case R.id.linear_step:
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(getActivity(),LoginActivity.class));break;
                }
                else {
                    startActivity(new Intent(getActivity(), Step_Map_Activity.class));break;
                }

            case R.id.linear_run:
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(getActivity(),LoginActivity.class));break;
                }
                else {
                    startActivity(new Intent(getActivity(), Step_Map_Activity.class));break;
                }

            case R.id.add_coffee:
                User_Data user_data=new User_Data();
                String string_drink=drink_num.getText().toString();
                String string_coffee=String.valueOf(Integer.valueOf(coffee_num.getText().toString())+1);
                String string_ka=tv_cur_ka.getText().toString();
                user_data.setUser_drink(string_drink);
                user_data.setUser_date(TimeUtil.getCurrentDate());
                user_data.setUser_coffee(string_coffee);
                user_data.setUser_ka(string_ka);
                coffee_num.setText(string_coffee);
                u_db.addNewUserData(user_data);break;

            case R.id.add_drink:
                User_Data user_data1=new User_Data();
                String string_drink1=String.valueOf(Integer.valueOf(drink_num.getText().toString())+1);
                String string_coffee1=coffee_num.getText().toString();
                String string_ka1=tv_cur_ka.getText().toString();
                user_data1.setUser_drink(string_drink1);
                user_data1.setUser_date(TimeUtil.getCurrentDate());
                user_data1.setUser_coffee(string_coffee1);
                user_data1.setUser_ka(string_ka1);
                drink_num.setText(string_drink1);
                u_db.addNewUserData(user_data1);break;

            case R.id.btn_eat:
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(getActivity(),LoginActivity.class));break;
                }
                else {
                    startActivity(new Intent(getActivity(), Eat_Activity.class));break;
                }


            case R.id.linear_eat:
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(getActivity(),LoginActivity.class));break;
                }
                else {
                    startActivity(new Intent(getActivity(), Eat_Activity.class));break;
                }


            case R.id.linear_weight:
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(getActivity(),LoginActivity.class));break;
                }
                else {
                    startActivity(new Intent(getActivity(), Weight_Activity.class));break;
                }



            case R.id.btn_weight:
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(getActivity(),LoginActivity.class));break;
                }
                else {
                    startActivity(new Intent(getActivity(), Weight_Add_Activity.class));break;
                }

            case R.id.grid_layout:
                RadioButton radioButton1=(RadioButton)getActivity().findViewById(R.id.tv_mall);
                RadioButton radioButton2=(RadioButton)getActivity().findViewById(R.id.tv_main);
                radioButton1.setChecked(true);
                radioButton1.setTextColor(getResources().getColor(R.color.orange));
                radioButton2.setTextColor(getResources().getColor(R.color.gray_default_dark));
                Fragment fragment1=new MallFragmen();
                fm.beginTransaction().replace(R.id.framelayout,fragment1).commit();break;

            case R.id.drink_:
                User_Data user_data2=new User_Data();
                String string_drink2=String.valueOf(Integer.valueOf(drink_num.getText().toString())-1);
                String string_coffee2=coffee_num.getText().toString();
                String string_ka2=tv_cur_ka.getText().toString();
                if (Integer.valueOf(string_drink2)<0){
                    string_drink2="0";
                }
                user_data2.setUser_drink(string_drink2);
                user_data2.setUser_date(TimeUtil.getCurrentDate());
                user_data2.setUser_coffee(string_coffee2);
                user_data2.setUser_ka(string_ka2);
                drink_num.setText(string_drink2);
                u_db.addNewUserData(user_data2);break;

            case R.id.coffee_:
                User_Data user_data3=new User_Data();
                String string_drink3=drink_num.getText().toString();
                String string_coffee3=String.valueOf(Integer.valueOf(coffee_num.getText().toString())-1);
                String string_ka3=tv_cur_ka.getText().toString();
                if (Integer.valueOf(string_coffee3)<0){
                    string_coffee3="0";
                }
                user_data3.setUser_drink(string_drink3);
                user_data3.setUser_date(TimeUtil.getCurrentDate());
                user_data3.setUser_coffee(string_coffee3);
                user_data3.setUser_ka(string_ka3);
                coffee_num.setText(string_coffee3);
                u_db.addNewUserData(user_data3);break;

            case R.id.linear_drink:
                startActivity(new Intent(getActivity(), Drink_Coffee_Activity.class));break;

            case R.id.linear_coffee:
                startActivity(new Intent(getActivity(), Drink_Coffee_Activity.class));break;

            case R.id.img_explore:
                if(edt_explore.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(),"请输入搜索内容！", Toast.LENGTH_LONG).show();
                }
                else {
                    Bundle bundle=new Bundle();
                    bundle.putString("searchtext",edt_explore.getText().toString());
                    bundle.putString("check","main");
                    Intent intent = new Intent(getActivity(),Mall_Search_Finally_Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.nowifi:
                Fragment fragment2=new MainFragment();
                fm.beginTransaction().replace(R.id.framelayout,fragment2).commit();break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!edt_explore.getText().toString().equals("")){
            getMallName(String.valueOf(s));
        }
        else {
            mall_names_list.clear();
            search_adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView textView= (TextView) view.findViewById(R.id.mall_search_item);
        edt_explore.setText(textView.getText());
    }



    private void getMallName(String mall_name) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("search",mall_name)
                .add("type", "main")
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mall_names_list=getMall("mall_name",json);
                            Log.i("mall_name", String.valueOf(mall_names_list));
                            search_adapter=new Search_adapter(getActivity(),mall_names_list);
                            search_list.setAdapter(search_adapter);
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

    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.runwoman,
                R.drawable.bycle,
                R.drawable.panpa,
        };


        @Override
        public View getView(ViewGroup container, int position) {
            RoundedImageView  view = new  RoundedImageView (container.getContext());

            view.setImageResource(imgs[position]);
            view.setCornerRadius(20f);
            view.setBorderWidth(2f);
            view.mutateBackground(true);
            view.setOval(false);
            view.setBorderColor(Color.parseColor("#2894FF"));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {//计时完毕时触发


            int  num=Integer.valueOf(txt_min.getText().toString());
            num--;
            txt_min.setText(String.valueOf(num));
            sale_TimeCount();

            if(num==0){
                txt_timeCount.setText("已结束");
                txt_min.setText("已结束");
            }


        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            txt_timeCount.setText(millisUntilFinished /1000+"秒");
        }
    }

    public String Plan_Count(){
        Frag_MainActivity.dialog_show(getActivity());
        OkHttpClient okHttpClient=new OkHttpClient();

        //创建一个Request
        final Request request = new Request.Builder()
                .url(Constant.CONNECTURL+"Plan_Count_Servlet")
                .get()
                .build();
        //封装成可执行的call对象
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                final Json_Tools json_tools=new Json_Tools();
                if(getActivity()!=null){
                    getActivity().runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Frag_MainActivity.dialog_cancle(getActivity());
                                        string=json_tools.Json_ToCount(json);
//                                    if(string.equals("0")){
//                                        txt_plan.setText("今天任务完成真棒！");
//                                        txt_plan.setTextColor(0x00EC00);
//                                    }
                                        if(string.equals("0")){
                                            txt_plan.setText("0");
                                        }
                                        txt_plan.setText(string);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                    );
                }



            }
        });
        return string ;
    }



    private void init(){
        tv_cur_ka.setText("0.00");
        drink_num.setText("0");
        coffee_num.setText("0");
        User_Data user_data=new User_Data();
        user_data.setUser_ka("0.00");
        user_data.setUser_drink("0");
        user_data.setUser_coffee("0");
        user_data.setUser_sleep("0");
        user_data.setUser_date(TimeUtil.getCurrentDate());
    }

}
