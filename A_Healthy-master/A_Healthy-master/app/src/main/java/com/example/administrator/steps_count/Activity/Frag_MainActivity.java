package com.example.administrator.steps_count.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.example.administrator.steps_count.Main_Activity.Plan_Activity;
import com.example.administrator.steps_count.Main_Activity.Plan_Add_Activity;
import com.example.administrator.steps_count.Main_Activity.Text_Activity;
import com.example.administrator.steps_count.R;
import com.example.administrator.steps_count.fragment.CircleFragment;
import com.example.administrator.steps_count.fragment.FootFragment;
import com.example.administrator.steps_count.fragment.MainFragment;
import com.example.administrator.steps_count.fragment.MallFragmen;
import com.example.administrator.steps_count.fragment.MeFragment;
import com.example.administrator.steps_count.model.User;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.MobileInfoUtils;
import com.example.administrator.steps_count.step.StepService;
import com.example.administrator.steps_count.tools.ShapeLoadingDialog;

import java.util.Timer;
import java.util.TimerTask;


public class Frag_MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FrameLayout frameLayout;
    private RadioButton tv_main,tv_mall,tv_foot,tv_circle,tv_me;

    public static User user;
    private MyBroadcost myBroadcost;
    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new Handler());

    private Button btn_startup;

    public static String localhost;

    public static boolean isLogin=false;
    public static String name="";
    public static String username="";
    public static String password="";
    public static String id="";
    private static ShapeLoadingDialog shapeLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_mainactivity);
        requestAllPower();


        setupService();

        localhost="192.168.43.37";

        SharedPreferences sp = getSharedPreferences("startup", MODE_PRIVATE);
        SharedPreferences sp1 = getSharedPreferences("user", Context.MODE_PRIVATE);
        user=new User();
        user.setUser_id(Integer.valueOf(sp1.getString("id","0" )));
        user.setUser_phone(sp1.getString("username","" ));
        user.setUsername(sp1.getString("name","" ));
        user.setPortrait(sp1.getString("portrait","" ));
        int startup_index = sp.getInt("startup_index",0);
        if(startup_index==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(Frag_MainActivity.this);
            View view = LayoutInflater.from(Frag_MainActivity.this).inflate(R.layout.startup_layout, null);
            builder.setView(view);
            final AlertDialog dialog = builder.show();
            view.findViewById(R.id.btn_startup).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            MobileInfoUtils.jumpStartInterface(Frag_MainActivity.this);
                            //创建sharedPreference对象，info表示文件名，MODE_PRIVATE表示访问权限为私有的
                            SharedPreferences sp = getSharedPreferences("startup", MODE_PRIVATE);

                            //获得sp的编辑器
                            SharedPreferences.Editor ed = sp.edit();

                            //以键值对的显示将用户名和密码保存到sp中
                            ed.putInt("startup_index", 1);

                            //提交用户名和密码
                            ed.commit();

                            dialog.dismiss();
                        }
                    }
            );

            view.findViewById(R.id.no).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    }
            );
        }


        myBroadcost=new MyBroadcost();


        IntentFilter intentFilter=new IntentFilter("android.intent.action.Broadcast");
        registerReceiver(myBroadcost,intentFilter);
        dynamicFragment(new MainFragment(),"mainFragment");
        initView();
        tv_main.setChecked(true);
    }

    public static void dialog_show(Context context){
        shapeLoadingDialog = new ShapeLoadingDialog.Builder(context)
                .loadText("加载中...")
                .build();
        shapeLoadingDialog.show();
    }
    public static void dialog_cancle(Context context){
        shapeLoadingDialog.cancel();
    }

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);

    }

    /**
     * 定时任务
     */
    private TimerTask timerTask;
    private Timer timer;
    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            /**
             * 设置定时器，每个三秒钟去更新一次运动步数
             */

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        Messenger messenger = new Messenger(service);
                        Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                        msg.replyTo = mGetReplyMessenger;
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            };

            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcost);
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) this.unbindService(conn);
    }



    public void initView(){
        frameLayout=(FrameLayout)findViewById(R.id.framelayout);
        tv_main= (RadioButton) findViewById(R.id.tv_main);
        tv_mall= (RadioButton) findViewById(R.id.tv_mall);
        tv_foot= (RadioButton) findViewById(R.id.tv_foot);
        tv_circle= (RadioButton) findViewById(R.id.tv_circle);
        tv_me= (RadioButton) findViewById(R.id.tv_me);

        tv_main.setOnClickListener(this);
        tv_mall.setOnClickListener(this);
        tv_foot.setOnClickListener(this);
        tv_circle.setOnClickListener(this);
        tv_me.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_main:
                tv_main.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_mall.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_circle.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_foot.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_me.setTextColor(getResources().getColor(R.color.gray_default_dark));
                MainFragment mainFragment=new MainFragment();
                if(!mainFragment.isAdded()){
                    dynamicFragment(mainFragment,"mainFragment");break;
                }

            case R.id.tv_mall:
                tv_mall.setTextColor(getResources().getColor(R.color.orange));
                tv_main.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_circle.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_foot.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_me.setTextColor(getResources().getColor(R.color.gray_default_dark));
                MallFragmen mallFragment=new MallFragmen();
                if(!mallFragment.isAdded()){
                    dynamicFragment(new MallFragmen(),"mallFragment");break;
                }
//                if (Frag_MainActivity.user.getUser_phone().equals("")) {
//                    startActivity(new Intent(Frag_MainActivity.this,LoginActivity.class));
//                    break;
//                }
//                else {
//                    MallFragmen mallFragment=new MallFragmen();
//                    if(!mallFragment.isAdded()){
//                        dynamicFragment(new MallFragmen(),"mallFragment");break;
//                    }
//                }


            case R.id.tv_foot:
                tv_foot.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_mall.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_circle.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_main.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_me.setTextColor(getResources().getColor(R.color.gray_default_dark));
                FootFragment footFragment=new FootFragment();
                if (!footFragment.isAdded()){
                    dynamicFragment(new FootFragment(),"footFragment");break;
                }
//                if (Frag_MainActivity.user.getUser_phone().equals("")) {
//                    startActivity(new Intent(Frag_MainActivity.this,LoginActivity.class));break;
//                }
//                else {
//                    FootFragment footFragment=new FootFragment();
//                    if (!footFragment.isAdded()){
//                        dynamicFragment(new FootFragment(),"footFragment");break;
//                    }
//                }


            case R.id.tv_circle:
                tv_mall.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_main.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_foot.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_me.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_circle.setTextColor(getResources().getColor(R.color.colorPrimary));
                CircleFragment circleFragment=new CircleFragment();
                if (!circleFragment.isAdded()){
                    dynamicFragment(new CircleFragment(),"circleFragment"); break;
                }
//                if (Frag_MainActivity.user.getUser_phone().equals("")) {
//                    startActivity(new Intent(Frag_MainActivity.this,LoginActivity.class));break;
//                }
//                else {
//                    CircleFragment circleFragment=new CircleFragment();
//                    if (!circleFragment.isAdded()){
//                        dynamicFragment(new CircleFragment(),"circleFragment"); break;
//                    }
//                }


            case R.id.tv_me:
                tv_me.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_mall.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_circle.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_foot.setTextColor(getResources().getColor(R.color.gray_default_dark));
                tv_main.setTextColor(getResources().getColor(R.color.gray_default_dark));
                MeFragment meFragment=new MeFragment();
                if(!meFragment.isAdded()){
                    dynamicFragment(new MeFragment(),"meFragment");break;
                }
//                if (Frag_MainActivity.user.getUser_phone().equals("")) {
//                    startActivity(new Intent(Frag_MainActivity.this,LoginActivity.class));break;
//                }
//                else {
//                    MeFragment meFragment=new MeFragment();
//                    if(!meFragment.isAdded()){
//                        dynamicFragment(new MeFragment(),"meFragment");break;
//                    }
//                }


            case R.id.add_plan:
                //Util.openApp("com.netease.cloudmusic",Frag_MainActivity.this);
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(Frag_MainActivity.this,LoginActivity.class));break;
                }
                else {
                    startActivity(new Intent(this,Plan_Add_Activity.class));break;
                }

            case R.id.fin_plan:
                if (Frag_MainActivity.user.getUser_phone().equals("")) {
                    startActivity(new Intent(Frag_MainActivity.this,LoginActivity.class));break;
                }
                else {
                    Intent intent=new Intent(Frag_MainActivity.this,Plan_Activity.class);
                    startActivity(intent);break;
                }


            case R.id.text_more:
                startActivity(new Intent(this,Text_Activity.class));break;
            case R.id.fin_text:
                startActivity(new Intent(this,Text_Activity.class));break;
        }
    }

    //动态添加Fragment
    public void dynamicFragment(android.support.v4.app.Fragment fragment,String tag){
        //1.获取碎片管理器
        FragmentManager supportFramentManager=getSupportFragmentManager();
        //2.开启一个事务
        FragmentTransaction beginTransaction=supportFramentManager.beginTransaction();
        //3.添加碎片
        beginTransaction.replace(R.id.framelayout,fragment,tag);
        //4.提交事务
        beginTransaction.commitAllowingStateLoss();
    }
    class  MyBroadcost extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }


    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

}
