package com.example.administrator.steps_count.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.steps_count.Main_Activity.Food;
import com.example.administrator.steps_count.Main_Activity.Plan;
import com.example.administrator.steps_count.Main_Activity.Text;
import com.example.administrator.steps_count.Main_Activity.TextResult;
import com.example.administrator.steps_count.Main_Activity.U_Food;
import com.example.administrator.steps_count.step.Constant;
import com.example.administrator.steps_count.step.StepEntity;
import com.example.administrator.steps_count.step.StepPlan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Json_Tools {
    public  String Step_ToJson(List<StepEntity> stepEntityList) throws JSONException {
        if (stepEntityList== null) return "";
        JSONArray array = new JSONArray();
        JSONObject jsonObject ;
        StepEntity stepEntity ;
        for (int i = 0; i < stepEntityList.size(); i++) {
            stepEntity = stepEntityList.get(i);
            jsonObject = new JSONObject();
            jsonObject.put("date", stepEntity.getCurDate());
            jsonObject.put("step", stepEntity.getSteps());
            jsonObject.put("id", stepEntity.getId());
            jsonObject.put("ka", stepEntity.getTotalStepsKa());
            jsonObject.put("km", stepEntity.getTotalStepsKm());
            array.put(jsonObject);
        }
        return array.toString();
    }

    public  String String_ToJson(String string) throws JSONException {
        if (string== null) return "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",string);

        return jsonObject.toString();
    }

    public String Json_ToCount(String data) throws JSONException {
        String string;
        JSONObject jsonObject;
        jsonObject=new JSONObject(data);
        string=jsonObject.get("count").toString();
        return string;
    }

    public StepPlan Json_To_StepPlan(String data) throws JSONException {
        StepPlan stepPlan=new StepPlan();
        JSONObject jsonObject = new JSONObject(data);
        stepPlan.setP_steps(jsonObject.getString("p_steps"));
        stepPlan.setP_km(jsonObject.getString("p_km"));
        stepPlan.setP_ka(jsonObject.getString("p_ka"));
        stepPlan.setU_id(jsonObject.getInt("u_id"));
        return stepPlan;
    }

    public  String Plan_ToJson(Plan plan) throws JSONException {
        if (plan== null) return "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("p_name",plan.getP_name());
        jsonObject.put("p_select",plan.getP_select());
        jsonObject.put("p_type",plan.getP_type());

        return jsonObject.toString();
    }

    public  String Food_ToJson(Food food) throws JSONException {
        if (food== null) return "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("f_name",food.getF_name());
        jsonObject.put("f_ka",food.getF_ka());
        jsonObject.put("f_type",food.getF_type());

        return jsonObject.toString();
    }

    public  String U_Food_ToJson(U_Food food) throws JSONException {
        if (food== null) return "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("u_id",food.getU_id());
        jsonObject.put("f_name",food.getF_name());
        jsonObject.put("f_ka",food.getF_ka());
        jsonObject.put("f_time",food.getF_time());
        jsonObject.put("f_date",food.getF_date());
        jsonObject.put("f_ke",food.getF_ke());

        return jsonObject.toString();
    }

    public  String StepPlan_ToJson(StepPlan stepPlan) throws JSONException {
        if (stepPlan== null) return "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("p_steps",stepPlan.getP_steps());
        jsonObject.put("p_km",stepPlan.getP_km());
        jsonObject.put("p_ka",stepPlan.getP_ka());
        jsonObject.put("u_id",stepPlan.getU_id());

        return jsonObject.toString();
    }

    public  String Plan_ToJson2(Plan plan) throws JSONException {
        if (plan== null) return "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("p_name",plan.getP_name());
        jsonObject.put("p_select",plan.getP_select());

        return jsonObject.toString();
    }

    public List<Plan> Json_ToPlan(String key, String jsonString) {
        List<Plan> list = new ArrayList<Plan>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);

            for (int i = 0; i < Persons.length(); i++) {
                Plan plan = new Plan();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                plan.setP_name(jsonObject2.getString("p_name"));
                plan.setP_type(jsonObject2.getString("p_type"));
                plan.setP_select(jsonObject2.getInt("p_select"));
                list.add(plan);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Food> Json_ToFood(String key, String jsonString) {
        List<Food> list = new ArrayList<Food>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);

            for (int i = 0; i < Persons.length(); i++) {
                Food food = new Food();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                food.setF_name(jsonObject2.getString("f_name"));
                food.setF_ka(jsonObject2.getString("f_ka"));
                food.setF_type(jsonObject2.getString("f_type"));
                list.add(food);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<U_Food> Json_ToUFood(String key, String jsonString) {
        List<U_Food> list = new ArrayList<U_Food>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);

            for (int i = 0; i < Persons.length(); i++) {
                U_Food food = new U_Food();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                food.setU_id(jsonObject2.getInt("u_id"));
                food.setF_name(jsonObject2.getString("f_name"));
                food.setF_ka(jsonObject2.getString("f_ka"));
                food.setF_time(jsonObject2.getString("f_time"));
                food.setF_date(jsonObject2.getString("f_date"));
                food.setF_ke(jsonObject2.getString("f_ke"));
                list.add(food);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<TextResult> Json_To_TextResult(String key, String jsonString) {
        List<TextResult> list = new ArrayList<TextResult>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);

            for (int i = 0; i < Persons.length(); i++) {
                TextResult textResult = new TextResult();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                textResult.setT_type(jsonObject2.getString("t_type"));
                textResult.setAnswer(jsonObject2.getString("answer"));
                textResult.setResult(jsonObject2.getString("result"));
                list.add(textResult);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Text> Json_ToText(String key, String jsonString) {
        List<Text> list = new ArrayList<Text>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray Persons = jsonObject.getJSONArray(key);

            for (int i = 0; i < Persons.length(); i++) {
                Text text = new Text();
                JSONObject jsonObject2 = Persons.getJSONObject(i);
                text.setT_type(jsonObject2.getString("t_type"));
                text.setT_id(jsonObject2.getInt("t_id"));
                text.setT_tittle(jsonObject2.getString("t_tittle"));
                text.setT_A(jsonObject2.getString("t_A"));
                text.setT_B(jsonObject2.getString("t_B"));
                text.setT_C(jsonObject2.getString("t_C"));
                list.add(text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public  String Json_To_String(String data) throws JSONException {
        String string;
        JSONObject jsonObject;
        jsonObject=new JSONObject(data);
        string= jsonObject.getString("portrait");
        return string;

    }

    //检测上传数据是是否有网络
    public boolean isNetworkAvailable(Context context) {
        // 获得网络状态管理器
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            // 建立网络数组
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
