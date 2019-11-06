package com.example.shield;

/**
 * Created by effy on 2018/11/13.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class request {
    public static Boolean Exist(final String phone_no){
        String result;
        Boolean exist =false;
        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)   //提交参数电话和密码
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/SignUpServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String res = jsonObject.getString("exist");
                if(res.equals("true")){
                    exist =true;
                }else{
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return exist;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User Login(final String phone_no) {
        User user = new User();

        String result;

        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)   //提交参数电话和密码
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/LoginServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            user = JX(result);    //解析
            System.out.println(user);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Boolean CompleteInfo(final String first_name,final String second_name,final String phone_no,String secureCode){
        String result;
        Boolean rs=false;

        FormBody body =new FormBody.Builder()
                .add("FName",first_name)
                .add("LName",second_name)
                .add("PhoneNo",phone_no)
                .add("SecureNo",secureCode)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/CompleteInfoServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String res = jsonObject.getString("result");
                if(res.equals("true")){
                    rs =true;
                }else{
                    rs =false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return rs;
    }

    private static User JX(String date){
        User user = new User();
        try {

            JSONObject jsonObject=new JSONObject(date);
            ArrayList<String> contactList = new ArrayList<>();
            ArrayList<String> supervisorList = new ArrayList<>();
            String res = jsonObject.getString("Result");//获取返回值Result的内容
            if (res.equals("success")){
                user.setFirstName(jsonObject.getString("first_name"));
                user.setSecondName(jsonObject.getString("last_name"));
                user.setPhoneNo(jsonObject.getString("phone_no"));
                user.setSecureCode(jsonObject.getString("secureCode"));
                JSONArray contact = jsonObject.getJSONArray("contactList");
                for(int i=0;i<contact.length();i++){
                    contactList.add(contact.getString(i));
                }
                user.setContactList(contactList);
                JSONArray supervisor = jsonObject.getJSONArray("supervisorList");
                for(int i=0;i<supervisor.length();i++){
                    supervisorList.add(supervisor.getString(i));
                }
                user.setSuperviseeList(supervisorList);
                System.out.println("parse successfully");
                System.out.println(user);
            }else{
                user=null;

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public static User AddEmergency(final String phone_no,final String Ephone_no) {
        User user = new User();
        String result;
        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)
                .add("EPhoneNo",Ephone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/AddEmergencyServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {


                JSONObject jsonObject=new JSONObject(result);
                String rs = jsonObject.getString("result");

                if (rs.equals("true")){

                    String first_name = jsonObject.getString("first_name");
                    String last_name = jsonObject.getString("last_name");
                    user.setPhoneNo(Ephone_no);
                    user.setFirstName(first_name);
                    user.setSecondName(last_name);
                }else{
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return user;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Boolean DeleteEmergency(final String phone_no, final String Ephone_no){
        Boolean rs=true;
        String result;
        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)
                .add("EPhoneNo",Ephone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/DeleteEmergencyServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String res = jsonObject.getString("result");
                if(res.equals("true")){
                    rs =true;
                }else{
                    rs =false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return rs;
    }


    public static ArrayList<User> ShowEmergency(final String phone_no){
        ArrayList<User> list = new ArrayList<>();
        String result;
        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/ShowEmergencyServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String res = jsonObject.getString("emergency");

                System.out.println("=============\n"+res);
                if(res == null){


                    return list;
                }else {
                    if(res.contains(";")){
                        String[] emer = res.split(";");
                        for(int i=0;i<emer.length;i++){
                            User user = new User();
                            if (emer[i].contains(":")) {
                                String[] name = emer[i].split(":");
                                if (name[0].contains("_")) {
                                    String[] n = name[0].split("_");
                                    System.out.println("=============================");
                                    System.out.println(res);
                                    System.out.println(name[0]);



                                    System.out.println(name.length);




                                    String phone = name[1];
                                    System.out.println(phone);

                                    System.out.println(n.length);


                                    System.out.println("=============================");

                                    String first_name = n[0];


                                    String second_name = n[1];


                                    user.setFirstName(first_name);
                                    user.setSecondName(second_name);
                                    user.setPhoneNo(phone);



                                    user.setContactList(null);
                                    user.setSuperviseeList(null);
                                    user.setSecureCode(null);



                                }
                            }
                            list.add(user);
                        }
                    }else{
                        User user = new User();
                        if (res.contains(":")) {
                            String[] name = res.split(":");
                            if (name[0].contains("_")) {
                                String[] n = name[0].split("_");
                                String phone = name[1];
                                String first_name = n[0];
                                String second_name = n[1];
                                user.setFirstName(first_name);
                                user.setSecondName(second_name);
                                user.setPhoneNo(phone);
                                user.setContactList(null);
                                user.setSuperviseeList(null);
                                user.setSecureCode(null);
                            }
                        }
                        list.add(user);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }



            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static ArrayList<User> ShowSupervisee(final String phone_no){
        ArrayList<User> list = new ArrayList<>();
        String result;
        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/ShowSupervisorServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            result = response.body().string();
            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(result);
                String res = jsonObject.getString("emergency");
                if(res == null){
                    return list;
                }else{

                    System.out.println("==================\n"+"ShowSupervisee");
                    System.out.println(res);




                    if(res.contains(";")){
                        String[] sup = res.split(";");
                        for(int i=0;i<sup.length;i++){
                            User user = new User();
                            String[] name= sup[i].split(":");
                            String phone = name[1];
                            String[] n = name[0].split("_");
                            String first_name = n[0];
                            String second_name = n[1];
                            user.setFirstName(first_name);
                            user.setSecondName(second_name);
                            user.setPhoneNo(phone);
                            user.setContactList(null);
                            user.setSuperviseeList(null);
                            user.setSecureCode(null);
                            list.add(user);
                        }
                    }else{
                        User user = new User();
                        String[] name = res.split(":");
                        String phone = name[1];
                        String[] n = name[0].split("_");
                        String first_name = n[0];
                        String second_name = n[1];
                        user.setFirstName(first_name);
                        user.setSecondName(second_name);
                        user.setPhoneNo(phone);
                        user.setContactList(null);
                        user.setSuperviseeList(null);
                        user.setSecureCode(null);
                        list.add(user);
                    }

                    for(User u : list){
                        System.out.println(u.firstName+u.phoneNo);
                    }
                    System.out.println("==================\n"+"ShowSupervisee");

                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Boolean Push(final String phone_no){
        String result;
        Boolean is;
        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/PushServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String rs = jsonObject.getString("result");
                if(rs.equals("true")){
                    is = true;
                }else{
                    is =false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return is;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean Arrive(final String phone_no){
        String result;
        Boolean is;

        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/ArriveServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String rs = jsonObject.getString("result");
                if(rs.equals("true")){
                    is = true;
                }else{
                    is =false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return is;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean Danger(final String phone_no){
        String result;
        Boolean is;

        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/dangerServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String rs = jsonObject.getString("result");
                if(rs.equals("true")){
                    is = true;
                }else{
                    is =false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return is;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void UpdatePosition(double longi, double lati,String phone_no){
        String longitude = ""+longi;
        String latitude =""+lati;
        FormBody body =new FormBody.Builder()
                .add("Longitude",longitude)
                .add("Latitude",latitude)
                .add("PhoneNo",phone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/GetPosServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
    public static Boolean SavePosition(final String departure,final String destination,final String phone_no,final String dep,final String des){
        Boolean rs;
        String result;
        FormBody body =new FormBody.Builder()
                .add("Departure",departure)
                .add("Destination",destination)
                .add("PhoneNo",phone_no)
                .add("Dep",dep)
                .add("Des",des)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/SavePositionServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String res = jsonObject.getString("result");
                if(res.equals("true")){
                    rs =true;
                }else{
                    rs =false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getDepature(final String phone_no){
        String route;
        String result;
        FormBody body =new FormBody.Builder()
                .add("PhoneNo",phone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/PostPositionServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String res = jsonObject.getString("result");
                if(res.equals("true")){
                    String departure =jsonObject.getString("departure");
                    String destination =jsonObject.getString("destination");
                    String dep = jsonObject.getString("dep");
                    String des = jsonObject.getString("des");
                    route = departure+"_"+destination+"_"+dep+"_"+des+"_"+phone_no;
                }else{
                    route =null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return route;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String GetPosition(String sphone_no){
        String result;
        String position=null;
        FormBody body =new FormBody.Builder()
                .add("SPhoneNo",sphone_no)
                .build();
        Request request = new Request.Builder()
                .url("http://101.132.96.45:8080/shield/PositionServlet")  //请求的地址
                .post(body)
                .build();
        OkHttpClient client=new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            try {
                JSONObject jsonObject=new JSONObject(result);
                String departure = jsonObject.getString("departure");
                String destination = jsonObject.getString("destination");
                String dep = jsonObject.getString("dep");
                String des = jsonObject.getString("des");
                String longitude = jsonObject.getString("longitude");
                String latitude =jsonObject.getString("latitude");
                position = departure+"_"+destination+"_"+dep+"_"+des+"_"+longitude+"_"+latitude;
                return position;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }

//    public static ArrayList<String> GetPosition(String phone_no){
//        String result;
//        ArrayList<String> position = new ArrayList<>();
//        FormBody body =new FormBody.Builder()
//                .add("PhoneNo",phone_no)
//                .build();
//        Request request = new Request.Builder()
//                .url("http://101.132.96.45:8080/shield/PositionServlet")  //请求的地址
//                .post(body)
//                .build();
//        OkHttpClient client=new OkHttpClient();
//
//        try {
//            Response response = client.newCall(request).execute();
//            result = response.body().string();
//            try {
//                JSONObject jsonObject=new JSONObject(result);
//                JSONArray list = jsonObject.getJSONArray("list");
//                for(int i=0;i<list.length();i++){
//                    String sup = list.getString(i);
//                    String[] su = sup.split(":");
//                    String phone = su[1];
//                    JSONObject jsonObj=jsonObject.getJSONObject(phone);
//                    String longitude = jsonObj.getString("longitude");
//                    String latitude = jsonObj.getString("latitude");
//                    String departure = jsonObj.getString("departure");
//                    String destination = jsonObj.getString("destination");
//                    String dep = jsonObj.getString("dep");
//                    String des = jsonObj.getString("des");
//
//
//                    String pos = phone+"_"+longitude+"_"+latitude+"_"+departure+"_"+destination+"_"+dep+"_"+des;
//                    position.add(pos);
//                }
//                return position;
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return null;
//            }
//        } catch (IOException e1) {
//            e1.printStackTrace();
//            return null;
//        }
//    }
//



}