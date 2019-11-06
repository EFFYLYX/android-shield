package com.example.shield;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.logging.Logger;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushService;

/**
 * Created by effy on 2018/10/31.
 */

public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Log.d("TAG", "onReceive - " + intent.getAction());
        Log.d("TAG", "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Toast.makeText(context, "自定义消息" + content + "\nextra" + extra, Toast.LENGTH_SHORT).show();

            //TODO 可推送json过来，接收到再解析

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");

            Intent renderIntent = new Intent();
            String info = bundle.getString(JPushInterface.EXTRA_ALERT);

//            String type = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            renderIntent.setAction("com.dessert.mojito.CHANGE_STATUS");


            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            System.out.println("收到了通知"+content);
            System.out.println("收到了通知"+info);

            renderIntent.putExtra("content",info);

//            renderIntent.putExtra("type",type);
            context.sendBroadcast(renderIntent);




            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为

           String s= bundle.getString(JPushInterface.EXTRA_EXTRA);

           String[] a = new String[4];

            try {
                JSONObject jsonObject  = new JSONObject(s);
                a[0] = jsonObject.getString("phone_no");
                a[1] = jsonObject.getString("first_name");
                a[2] = jsonObject.getString("second_name");
                a[3] = jsonObject.getString("secure_no");




            } catch (JSONException e) {
                e.printStackTrace();
            }


            Intent i = new Intent(context, TabActivity.class); // 自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setAction("com.dessert.mojito.CHANGE_STATUS");
            i.putExtra("thisUser",a);

            String info = bundle.getString(JPushInterface.EXTRA_ALERT);
            i.putExtra("content",info);
         //   i.setAction("com.dessert.mojito.CHANGE_STATUS");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(i);

        //    context.sendBroadcast(i);


            ///获取intent url
//            Intent inten=new Intent(Intent.ACTION_VIEW, Uri.parse("myscheme://com.myhost.push/push_detail?message=what"));
//
//            inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            String intenturl=inten.toUri(Intent.URI_INTENT_SCHEME);
//
//            Log.e("intenturl","intenturl="+intenturl);

        } else {
            Log.d("TAG", "Unhandled intent - " + intent.getAction());
        }


    }

    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i("TAG", "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e("TAG", "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


}
