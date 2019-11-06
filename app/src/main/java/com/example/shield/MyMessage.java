package com.example.shield;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by effy on 2018/10/31.
 */

public class MyMessage {
    String phone;


    String type; //系统通知 or 用户通知
    String title;

    String content;
    Date time;

    Boolean isChecked = false;

    private static final String JSON_type="type";
    private static final String JSON_title="title";
    private static final String JSON_content="content";

    private static final String JSON_isChecked="isChecked";



    boolean hasRead = false;
    public MyMessage(String phone, String type, String title, String content){
        this.phone=phone;
        this.type = type;
        this.title = title;
        this.content = content;

    }

    public MyMessage(){

    }

    public JSONObject toJSON(){

        JSONObject json = new JSONObject();
        try {
            json.put(JSON_type,type);
            json.put(JSON_title,title);
            json.put(JSON_content,content);
            if (isChecked == true) {
                json.put(JSON_isChecked, "true");
            }else{
                json.put(JSON_isChecked,"false");
            }

        } catch (JSONException e) {
           return null;
        }


//        json.put(JSON_isChecked, isChecked);

        return json;
    }

    public MyMessage(JSONObject json){
        try {
            type= json.getString(JSON_type);

            title = json.getString(JSON_title);
            content = json.getString(JSON_content);
            String temp = json.getString(JSON_isChecked);

            if (temp.equals("true")){
                isChecked=true;
            }else{
                isChecked= false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}
