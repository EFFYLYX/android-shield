package com.example.shield;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.shield.Jpush.PollingTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by effy on 2018/10/30.
 */



public class SuperviseeFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener,Chronometer.OnChronometerTickListener{


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //    private OnFragmentInteractionListener mListener;
    private static com.example.shield.SupervisorFragment fragment = null;

    public SuperviseeFragment() {
        // Required empty public constructor
    }

//    public static SuperviseeFragment newInstance() {
//        if (fragment == null) {
//            synchronized (MapFragment.class) {
//                if (fragment == null) {
////                    fragment = new SuperviseeFragment();
//                }
//            }
//        }
//        return fragment;
//    }


    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;
    private static final int SETTING_REQUESTCODE = 1;

    private boolean isNeedCheck = true;


    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(getActivity(),
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
//        myLocation();

    }

    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(), perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private Chronometer chronometer;


    String[] user_arr;
    User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( getActivity().getIntent().hasExtra("thisUser")){
            user_arr = getActivity().getIntent().getStringArrayExtra("thisUser");

            Log.d("user_arr",user_arr[0]);

            mUser = new User(user_arr[0],user_arr[1],user_arr[2],user_arr[3]);

        }

    }


    private MapView mapView;
    private AMap aMap;
    private View mapLayout;
    private Button start_button;
    private Button end_button;
    private Button sos_button;

    MyLocationStyle myLocationStyle;

    private TextView et_departure;
    private TextView et_destination;

    TextView tv_name;

    ArrayList<User> emergency_contatcts = new ArrayList<>();

    public void myLocation() {


    }
    LinearLayout ll_start;
    LinearLayout ll_not_start;

    private TextView mint;
    private TextView sec;
    private long timeusedinsec;
    private boolean isstop = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (mapLayout == null) {
            Log.i("sys", "MF onCreateView() null");
            mapLayout = inflater.inflate(R.layout.fragment_supervisee, null);
            mapView = (MapView) mapLayout.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            if (aMap == null) {
                aMap = mapView.getMap();
            }
        } else {

            if (mapLayout.getParent() != null) {
                ((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
            }
        }



        iv = mapLayout.findViewById(R.id.iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (list.size()!=0) {

                    showRadioDialog();
                }
            }
        });
        tv_name = mapLayout.findViewById(R.id.tv_name);



        ll_start = mapLayout.findViewById(R.id.ll_in_trip);

        ll_not_start = mapLayout.findViewById(R.id.ll_not_start);

        ll_start.setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                ArrayList<User> emergency_contatcts = request.ShowEmergency(mUser.phoneNo);
                ArrayList<User> emergency_contatcts = request.ShowSupervisee(mUser.phoneNo);

                Message message = new Message();
                message.what=0;
                message.obj=emergency_contatcts;
                mhandle.sendMessage(message);

            }
        }).start();



        sos_button = mapLayout.findViewById(R.id.sos_button);
        sos_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:110"));
                startActivity(intent);


            }
        });


        end_button = mapLayout.findViewById(R.id.end_button);


//        mint = (TextView) mapLayout.findViewById(R.id.mint);
//        sec = (TextView) mapLayout.findViewById(R.id.sec);

        et_departure = mapLayout.findViewById(R.id.et_departure);

        et_departure.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        et_destination = mapLayout.findViewById(R.id.et_destination);

        et_destination.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线


//
//        BroadcastReceiver mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(intent.getAction().equals("com.dessert.mojito.CHANGE_STATUS")) {
//
//
//                    Message message = new Message();
//                    message.what =3;
//
//
//                    message.obj=intent.getStringExtra("content");
//
//
//                    Message msg = new Message();
//                    msg.what=2;
//                    message.obj=intent.getStringExtra("type");
//
//                    System.out.println("========my[[[ receiver check\n"+ intent.getStringExtra("content"));
//                    mhandle.sendMessage(message);
//                }
//            }
//        };
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.dessert.mojito.CHANGE_STATUS");
//        getActivity().registerReceiver(mReceiver, filter);


        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true); //缩放按钮的显示与隐藏
        mUiSettings.setZoomPosition(AMapOptions.LOGO_MARGIN_LEFT);
        mUiSettings.setCompassEnabled(false); //指南针的显示与隐藏
        mUiSettings.setScaleControlsEnabled(false); //比例尺的显示与隐藏
        mUiSettings.setLogoPosition(AMapOptions.LOGO_MARGIN_LEFT); //         设置LOGO位置
        mUiSettings.setRotateGesturesEnabled(false); //禁止旋转


//
//        chronometer = (Chronometer) mapLayout.findViewById(R.id.chronometer);
//        chronometer.setOnChronometerTickListener(this);

        ll_not_start.setVisibility(View.GONE);





        return mapLayout;

    }


    private UiSettings mUiSettings;

    ArrayList<User> list = new ArrayList<>();
    HashMap<String,User> userMap = new HashMap();//key 名字

    HashMap<String,User> phoneMap = new HashMap<>(); //key phone value User

    HashMap<String, String> oldPositionMap = new HashMap();


    HashMap<String, UserLocation> userLocationHashMap = new HashMap<>();  //key 名字




    private Handler mhandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 0:
                    ArrayList<User> temp = (ArrayList<User>) msg.obj;

                    if (temp == null){
//                        Toasty.error(getContext(), "No one helps you. ", Toast.LENGTH_SHORT, true).show();
                        Toasty.error(getContext(), "Waiting...Loading... ", Toast.LENGTH_SHORT, true).show();

                    }else {
                        //supervisorList = (List<User>) msg.obj;
                        for (User u : temp) {

                            if (u!=null) {


                                list.add(u);
                                userMap.put(u.getUsername(),u);
                                phoneMap.put(u.getPhoneNo(),u);

                                userLocationHashMap.put(u.getUsername(),new UserLocation(u));



                            }else{
                                Toasty.error(getContext(), "Waiting...Loading... ", Toast.LENGTH_SHORT, true).show();
                            }
//
                        }




                    }

                    break;
                case 1:


                    break;

                case 2:
                    String type = (String) msg.obj;
                    if (type.equals("start")){

//                        mPushPollingTask.createTask(2).connected().setOnTaskListener(new PollingTask.OnTaskListener() {
//                            @Override
//                            public void executeTask(PollingTask pollingTask) {
//
//
//
//
//                            }
//                        });

                    }
                    if (type.equals("danger")){

                    }

                    if (type.equals("safe")){

                    }

                    break;



                case 3:

                    String sss = (String) msg.obj;




//                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(getApplicationContext());
//                    alertDialogBuilder.setTitle("Notification");
//                    alertDialogBuilder.setMessage(s);
//                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    alertDialogBuilder.show();

//                    Toasty t = new Toasty();


                    Toasty.info(getContext(),sss, Toast.LENGTH_SHORT, true).show();






                    break;





                case 5:

                    String res = (String) msg.obj;

                    //   position = departure+"_"+destination+"_"+dep+"_"+des+"_"+longitude+"_"+latitude;

                    String[] res_array = res.split("_");

                    //判断是否开始了

                    System.out.println("========================");
                    System.out.println(res);

                    if (res_array[0].equals("null")){
                        isStart = false;



                        ll_not_start.setVisibility(View.VISIBLE);
                        ll_start.setVisibility(View.GONE);
                    }else{
                        isStart = true;

//                        aMap.clear();

                        ll_not_start.setVisibility(View.GONE);
                        ll_start.setVisibility(View.VISIBLE);
                    }

                    System.out.println("isStart?"+isStart);
                    System.out.println("Index?"+index);
//刚刚开始 没有一个点
                    if (isStart && index==0 ){

                        System.out.println("刚刚开始 没有一个点");
                        departure = res_array[0];

                       System.out.println("hasMark?"+hasMark);

                        destination = res_array[1];
                        et_departure.setText(departure);
                        et_destination.setText(destination);

                       hasMark=true;



                        String[] dep = res_array[2].split(",");



                        startPoint = new LatLng(Double.parseDouble(dep[0]),  Double.parseDouble(dep[1]));
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 10));
                         MarkerOptions start_mo = new MarkerOptions();
                         start_mo.position(startPoint);
                         start_mo.title("Departure");
                         startMarker = aMap.addMarker(start_mo);
                         startMarker.showInfoWindow();




                        String[] des = res_array[3].split(",");
                        endPoint = new LatLng(Double.parseDouble(des[0]),Double.parseDouble(des[1]));

                        MarkerOptions end_mo = new MarkerOptions();

                        end_mo.position(endPoint);
                        end_mo.title("Destination");
                        endMarker = aMap.addMarker(end_mo);
                        endMarker.showInfoWindow();

                        oldPoint = new LatLng(Double.parseDouble(res_array[5]),Double.parseDouble(res_array[4]));

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(oldPoint);
                        markerOptions.title("Now");
                        currentMarker = aMap.addMarker(markerOptions);
                        currentMarker.showInfoWindow();

                        index=1;





//                        Marker startMarker = new Marker();





                    }

                    //已经开始 存在一个点

                    if (isStart && index ==1 ){

                        System.out.println("已经开始 存在一个点");
                        System.out.println("hasMark?"+hasMark);
//                        aMap.clear();


//                        departure = res_array[0];
//
//
//
//                        destination = res_array[1];
//                        et_departure.setText(departure);
//                        et_destination.setText(destination);
//
//
//                        String[] dep = res_array[2].split(",");
//
//
//
//                        startPoint = new LatLng(Double.parseDouble(dep[0]),  Double.parseDouble(dep[1]));
//
//
//
//
//                        MarkerOptions start_mo = new MarkerOptions();
//                        start_mo.position(startPoint);
//                        start_mo.title("Departure");
//                        startMarker = aMap.addMarker(start_mo);
//                        startMarker.showInfoWindow();
//
//
//
//
//                        String[] des = res_array[3].split(",");
//                        endPoint = new LatLng(Double.parseDouble(des[0]),Double.parseDouble(des[1]));
//
//                        MarkerOptions end_mo = new MarkerOptions();
//
//                        end_mo.position(endPoint);
//                        end_mo.title("Destination");
//                        endMarker = aMap.addMarker(end_mo);
//                        endMarker.showInfoWindow();
//

                        LatLng newPoint =  new LatLng(Double.parseDouble(res_array[5]),Double.parseDouble(res_array[4]));
                        System.out.println("newpoint"+newPoint.toString());


//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(oldPoint);
//                        markerOptions.title("Now");
//                        currentMarker = aMap.addMarker(markerOptions);
//                        currentMarker.showInfoWindow();

                        moverCar(currentMarker,newPoint);

                        oldPoint=newPoint;




                    }




                    //有点了 但是结束了
                    if (!isStart && index ==1){
                        System.out.println("有点了 但是结束了");




                        ll_not_start.setVisibility(View.VISIBLE);
                        ll_start.setVisibility(View.GONE);

                        index =0;




                    }



//                    }
                    System.out.println("================================");
//


                    mPushPollingTask.notifyTaskFinish();
                    break;
            }


        }
    };



    Marker currentMarker;
    Marker startMarker;
    Marker endMarker;
    int index =0;
    private boolean isPause = false;//是否暂停
    private long currentSecond = 0;//当前毫秒数


String departure;
String destination;
LatLng startPoint;
LatLng endPoint;
LatLng oldPoint;


boolean isStart = false;
boolean hasTwo = false;

boolean hasMark = false;


    ImageView iv;


    private void showRadioDialog(){
        final String radioItems[] = new String[list.size()];
        int i =0;
        for (User u : list){
            radioItems[i] = u.getUsername();
            i++;

        }


        final AlertDialog.Builder radioDialog = new AlertDialog.Builder(getContext());
        radioDialog.setTitle("Pick A Supervisee");
//        radioDialog.setIcon(R.mipmap.ic_launcher_round);

    /*
        设置item 不能用setMessage()
        用setSingleChoiceItems
        items : radioItems[] -> 单选选项数组
        checkItem : 0 -> 默认选中的item
        listener -> 回调接口

        String
    */

    final String choose = new String();

        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                choose = radioItems[which];



               updateUI(radioItems[which]);
               hasMark=false;
               index=0;
               dialog.dismiss();
//                Toast.makeText(DialogActivity.this,radioItems[which],Toast.LENGTH_SHORT).show();
            }
        });



        //设置按钮
        radioDialog.setPositiveButton("Cancel"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aMap.clear();

                        mPushPollingTask.destroyTask();
//                        secondTask.destroyTask();
//
//                        updateUI(choose);
                        dialog.dismiss();
                    }
                });

        radioDialog.create().show();
    }



    LatLng start;
    LatLng end;



    PollingTask mPushPollingTask = new PollingTask();

    PollingTask secondTask = new PollingTask();

   LatLng current;




    public void updateUI(String radioItem){
//        final UserLocation userLocation = userLocationHashMap.get(radioItem);

        tv_name.setText(radioItem);

        final User u= userMap.get(radioItem);

        end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "tel:"+ u.phoneNo;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
                startActivity(intent);


//                ll_start.setVisibility(View.GONE);
//                ll_route.setVisibility(View.VISIBLE);
            }
        });



        mPushPollingTask.createTask(2).connected().setOnTaskListener(new PollingTask.OnTaskListener() {
            @Override
            public void executeTask(PollingTask pollingTask) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       String s =request.GetPosition(u.phoneNo);

                       Message msg = new Message();
                       msg.obj=s;
                       msg.what=5;
                       mhandle.sendMessage(msg);


                    }
                }).start();



            }
        });








 //       }



    }







    private void moverCar(final Marker marker, LatLng endLatLng){

        //1,获取到起点到终点的点，默认绘制4个点,最后一个为终点
        final List<LatLng> roads = new ArrayList<LatLng>();
        LatLng startLatLng = marker.getPosition();//小车当前位置
//        LatLng endLatLng = options.markerOptions.getPosition();//终点位置

        System.out.println("移动啦"+endLatLng.toString());

        LatLng latLng_2 = new LatLng((startLatLng.latitude+endLatLng.latitude)/2, (startLatLng.longitude+endLatLng.longitude)/2);
        LatLng latLng_1 = new LatLng((startLatLng.latitude+latLng_2.latitude)/2, (startLatLng.longitude+latLng_2.longitude)/2);
        LatLng latLng_3 = new LatLng((latLng_2.latitude+endLatLng.latitude)/2, (latLng_2.longitude+endLatLng.longitude)/2);

        roads.add(latLng_1);
        roads.add(latLng_2);
        roads.add(latLng_3);
        roads.add(endLatLng);
        Thread t =new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {

                    for (int i = 0; i < roads.size(); i++) {
                        marker.setPosition(roads.get(i));
                        Thread.sleep(200);
                    }

                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_placeholder));
//                    marker.setIcon(options.markerOptions.getIcon());

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    /**绘制两个坐标点之间的线段,从以前位置到现在位置*/
    private void setUpMap(LatLng oldData,LatLng newData ) {
        System.out.println("画曲线画曲线画曲线");
        //  绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(Color.BLUE));

    }






    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }


    }

    private void showMissingPermissionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("NO GPS");
        builder.setMessage("Please set up GPS permissions");

        // 拒绝, 退出应用
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();

                    }
                });

        builder.setPositiveButton("Setting",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();

    }

    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(intent, SETTING_REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTING_REQUESTCODE) {
            checkPermissions(needPermissions);
        }
    }


    @Override
    public void onResume() {
        Log.i("sys", "mf onResume");
        super.onResume();


        mapView.onResume();
        myLocation();

    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onPause() {
        Log.i("sys", "mf onPause");
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("sys", "mf onSaveInstanceState");
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onDestroy() {


        Log.i("sys", "mf onDestroy");

        super.onDestroy();

        if (mPushPollingTask.hasStart()){
            mPushPollingTask.destroyTask();
        }


        mapView.onDestroy();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        String time = chronometer.getText().toString();

        System.out.println(time);
//        if(time.equals("00:00")){
//            Toast.makeText(MainActivity.this,"时间到了~", Toast.LENGTH_SHORT).show();
//        }
//
    }
}




