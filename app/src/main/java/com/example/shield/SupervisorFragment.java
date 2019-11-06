package com.example.shield;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.shield.Jpush.PollingTask;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;


public class SupervisorFragment extends Fragment implements OnRequestPermissionsResultCallback, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, TextWatcher,
        PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener, LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    User mUser;

    //    private OnFragmentInteractionListener mListener;
    private static SupervisorFragment fragment = null;

    public SupervisorFragment() {
        // Required empty public constructor
    }

    public static SupervisorFragment newInstance() {
        if (fragment == null) {
            synchronized (MapFragment.class) {
                if (fragment == null) {
                    fragment = new SupervisorFragment();
                }
            }
        }
        return fragment;
    }


    protected String[] needPermissions = {

            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mUser = new User("18013523801", "David", "Beckham", "1234");
        isFirstLatLng = true;

    }

    private LatLng oldLatLng;
    //是否是第一次定位
    private boolean isFirstLatLng;
    private MapView mapView;
    private AMap aMap;
    private View mapLayout;
    private Button start_button;
    private Button end_button;
    private Button sos_button;

    MyLocationStyle myLocationStyle;

    private EditText et_departure;
    private EditText et_destination;

    private EditText et_change_destination;
    String city = "";

    private UiSettings mUiSettings;
    private AMapLocationClient mlocationClient = null;

    LatLng departure_latlang;

    /**
     * 声明mLocationOption对象
     */
    private AMapLocationClientOption mLocationOption = null;
    private LatLonPoint startPoint;
    private LatLonPoint endPoint;

    public void myLocation() {

        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);


        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true); //缩放按钮的显示与隐藏
        mUiSettings.setZoomPosition(AMapOptions.LOGO_MARGIN_LEFT);
        mUiSettings.setCompassEnabled(false); //指南针的显示与隐藏
        mUiSettings.setScaleControlsEnabled(false); //比例尺的显示与隐藏
        mUiSettings.setLogoPosition(AMapOptions.LOGO_MARGIN_LEFT); //         设置LOGO位置
        mUiSettings.setRotateGesturesEnabled(false); //禁止旋转


    }

    boolean hasClicked = false;

    PollingTask mPushPollingTask = new PollingTask();

    String[] user_arr;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        mapLayout = inflater.inflate(R.layout.fragment_supervisor, null);

//        xPermissions();

        if (getActivity().getIntent().hasExtra("thisUser")) {
            user_arr = getActivity().getIntent().getStringArrayExtra("thisUser");

            Log.d("user_arr", user_arr[0]);

            mUser = new User(user_arr[0], user_arr[1], user_arr[2], user_arr[3]);

        }


        if (mapLayout == null) {
            Log.i("sys", "MF onCreateView() null");
            mapLayout = inflater.inflate(R.layout.fragment_supervisor, null);
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

        myLocation();


        FloatingActionButton fab1 = (FloatingActionButton) mapLayout.findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!hasClicked) {
                    testLoopPlayer();
                    hasClicked = true;
                } else {
                    mPlayer.stop();
                    hasClicked = false;
                }

            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) mapLayout.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasClicked) {
                    mPlayer.stop();
                    hasClicked=false;
                }
                startActivity(new Intent(getActivity(), FakeCallActivity.class));


            }
        });


        final LinearLayout ll_route = mapLayout.findViewById(R.id.ll_route);
        final LinearLayout ll_start = mapLayout.findViewById(R.id.ll_in_trip);


        ll_start.setVisibility(View.GONE);


        start_button = mapLayout.findViewById(R.id.start_button);


        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_route.setVisibility(View.GONE);
                ll_start.setVisibility(View.VISIBLE);


                if (startChanged == false) {

                    LatLng start = new LatLng(mLocation.latitude, mLocation.longitude);


                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(start);
                    markerOption.zIndex(-1);
                    markerOption.title("Departure");

                    aMap.addMarker(markerOption);
                }

                searchRoute();

                new Thread(new Runnable() {

                                                       @Override


                                                       public void run() {


                                                           Boolean b = request.Push(mUser.phoneNo);
                                                           System.out.println("push success?" + b);

                                                           Message msg = new Message();
                                                           msg.what = 0;
                                                           msg.obj=b;
                                                           handler.sendMessage(msg);
                                                       }
                                                   }).start();




            }
        });


        sos_button = mapLayout.findViewById(R.id.sos_button);
        sos_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Boolean b = request.Danger(mUser.phoneNo);
                        System.out.println("push success?" + b);
                    }
                }).start();

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:110"));
                startActivity(intent);
            }
        });


        end_button = mapLayout.findViewById(R.id.end_button);


        end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View passwordView = null;


                if (passwordView == null) {

                    passwordView = inflater.inflate(R.layout.attrive_safe, null);
                    Log.i("sys", "MF onCreateView() null");


                } else {

                    if (passwordView.getParent() != null) {
                        ((ViewGroup) passwordView.getParent()).removeView(passwordView);
                    }
                }
                final EditText p1 = passwordView.findViewById(R.id.et_p1);


                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Check Permission")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                String p = p1.getText().toString();

                                if (p.equals("1234")) {


                                    et_departure.setText("Current Location");

                                    et_destination.setText("");

                                    ll_start.setVisibility(View.GONE);
                                    ll_route.setVisibility(View.VISIBLE);
                                    if (polyline != null) {
                                        polyline.remove();
                                        polyline = null;

                                        aMap.clear();
                                    }

                                    startChanged = false;
                                    if (mPushPollingTask.hasStart()) {

                                        mPushPollingTask.destroyTask();
                                    }
                                    Toasty.success(getContext(), "Arrived safely！", Toast.LENGTH_SHORT, true).show();

                                    new Thread(new Runnable() {

                                        @Override
                                        public void run() {

                                            Boolean b = request.Arrive(mUser.phoneNo);
                                            System.out.println("push success?" + b);

                                        }
                                    }).start();


                                } else {
                                    Toasty.error(getContext(), "Wrong Secure Passwords!", Toast.LENGTH_SHORT, true).show();

                                }

                            }
                        })
                        .setView(passwordView)
                        .show();


            }
        });


        checkPermissions(needPermissions);


        et_departure = mapLayout.findViewById(R.id.et_departure);
        et_departure.setFocusableInTouchMode(false);
        et_departure.setFocusable(false);
        et_departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("city", city);

                intent.putExtra("requestcode", "321");
                startActivityForResult(intent, 321);
//                startActivity(new Intent(getActivity(),SearchActivity.class));
            }
        });

        et_destination = mapLayout.findViewById(R.id.et_destination);
        et_destination.setFocusable(false);
        et_destination.setFocusableInTouchMode(false);


        et_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("city", city);
                intent.putExtra("requestcode", "123");
                startActivityForResult(intent, 123);
            }
        });


        et_change_destination = mapLayout.findViewById(R.id.et_change_destination);
        et_change_destination.setFocusable(false);
        et_change_destination.setFocusableInTouchMode(false);


        return mapLayout;

    }


//    PoiAddressBean startPoint;
//    PoiAddressBean endPoint;




    public void searchRoute() {
//        startPoint = new LatLonPoint(mLocation.latitude,mLocation.longitude);


        RouteSearch routeSearch = new RouteSearch(getActivity());
        routeSearch.setRouteSearchListener(this);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(
                fromAndTo, //路径规划的起点和终点
                RouteSearch.DrivingDefault, //驾车模式
                null, //途经点
                null, //示避让区域
                "" //避让道路
        );
        routeSearch.calculateDriveRouteAsyn(query);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
//        List<DrivePath> drivePathList = result.getPaths();
        List<DrivePath> drivePathList = result.getPaths();


        DrivePath drivePath = drivePathList.get(0);

        List<DriveStep> steps = drivePath.getSteps();

        List<LatLng> plist = new ArrayList<>();
        for (DriveStep step : steps) {

            List<LatLonPoint> polyline = step.getPolyline();
            for (LatLonPoint p : polyline) {
                System.out.println("Drvie path----------" + drivePathList.size());

                plist.add(new LatLng(p.getLatitude(), p.getLongitude()));

            }

        }

//        aMap.clear();
        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }

        if (plist.size() > 2) {


            LatLng oldL = plist.get(0);
            LatLng newL = null;

            for (int i = 1; i < plist.size(); i++) {
                newL = plist.get(i);

                polyline = aMap.addPolyline((new PolylineOptions())
                        .add(oldL, newL)
                        .geodesic(true).color(Color.BLUE));

                oldL = newL;
            }
        }


    }

    Polyline polyline;

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }


    private MediaPlayer mPlayer, mNextPlayer;
    private int mPlayResId = R.raw.police;


    public void testLoopPlayer() {

        mPlayer = MediaPlayer.create(getActivity(), mPlayResId);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mPlayer.start();
            }
        });
        createNextMediaPlayer();
    }

    private void createNextMediaPlayer() {


        mPlayer.setVolume(1.0f, 1.0f);

//        mPlayer.stop();audioMgr
        mNextPlayer = MediaPlayer.create(getActivity(), mPlayResId);
        mPlayer.setNextMediaPlayer(mNextPlayer);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();

                mPlayer = mNextPlayer;

                createNextMediaPlayer();
            }
        });
    }


    TimerTask mTimerTask;
    int mInt;

    Timer mTimer;

    Boolean start_share = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    flashOn();
                    break;
                case 0:

                    Boolean b = (Boolean) msg.obj;


                    if (b) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String dep = startPoint.getLatitude()+","+startPoint.getLongitude();
                                String des = endPoint.getLatitude()+","+endPoint.getLongitude();



                                Boolean b = request.SavePosition(departure, destination, mUser.phoneNo,dep,des);
                                Message message = new Message();
                                message.what = 2;
                                message.obj = b;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }

                  break;

                case 2:

                    Boolean c = (Boolean) msg.obj;
                    if (c){

                        Log.d("share location","sharing!");
                        start_share = true;

                     //   if (start_share){
                            mPushPollingTask.createTask(2)
                                    .connected()
                                    .setOnTaskListener(new PollingTask.OnTaskListener() {
                                                           @Override
                                                           public void executeTask(PollingTask pollingTask) {
                                                               // 执行一个任务，可以是同步的也可以是异步的
                                                               // 比如获取推送的新闻信息
//                                                   getPushNewsInfos()

                                                               new Thread(new Runnable() {

                                                                   @Override


                                                                   public void run() {


                                                                       request.UpdatePosition(mLocation.longitude,mLocation.latitude,mUser.phoneNo);

                                                                       Message msg = new Message();
                                                                       msg.what = 5;
                                                                       handler.sendMessage(msg);
                                                                   }
                                                               }).start();

                                                           }
                                                       }
                                    );



             //           }
                    }

                    break;
//                    flashOff();
                case 5:
                    mPushPollingTask.notifyTaskFinish();
                    break;
            }
        }
    };


    PoiAddressBean startBean;
    PoiAddressBean endBean;


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

    Marker start_marker;

    String departure;
    String destination;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 321) {


            Bundle bundle = data.getExtras();
            if (bundle != null) {
                System.out.print("-------------===========================");
                System.out.print(bundle.getString("address"));
                if (!bundle.getString("address").equals("//")) {

                    if (start_marker != null) {
                        start_marker.remove();
                    }


                    et_departure.setText(bundle.getString("address"));
                    departure=bundle.getString("address");
                    String[] strings = bundle.getStringArray("bean");

                    startPoint = new LatLonPoint(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                    startChanged = true;
                    LatLng start = new LatLng(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));


                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(start);
                    markerOption.zIndex(-1);
                    markerOption.title("Departure");
                    start_marker = aMap.addMarker(markerOption);

                    System.out.print(bundle.getStringArray("bean")[1]);
                }


            }


        }

        if (requestCode == 123) {


            Bundle bundle = data.getExtras();
            if (bundle != null) {
                System.out.println("-------------===========================");
                System.out.print(bundle.getString("address"));
                if (!bundle.getString("address").equals("//")) {

                    et_change_destination.setText(bundle.getString("address"));
                    et_destination.setText(bundle.getString("address"));

                    destination=bundle.getString("address");
                    String[] strings = bundle.getStringArray("bean");

                    endPoint = new LatLonPoint(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));

                    LatLng start = new LatLng(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));


                    if (dest_maker != null) {
                        dest_maker.remove();
                    }


                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(start);
                    markerOption.zIndex(-1);
                    markerOption.title("Destination");
                    dest_maker = aMap.addMarker(markerOption);


//                        endPoint = new LatLng()

                    System.out.print(bundle.getStringArray("bean")[1]);
                }
            }

        }

        if (requestCode == SETTING_REQUESTCODE) {
            checkPermissions(needPermissions);
        }


    }

    Marker dest_maker;

    private void clearMarkers() {
        //获取地图上所有Marker
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker = mapScreenMarkers.get(i);
//            if (marker.getObject() instanceof xxx) {
//                marker.remove();//移除当前Marker
//            }
        }
//        aMap.
//        aMap.invalidate();//刷新地图
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

        mlocationClient.unRegisterLocationListener(this);
        mlocationClient = null;
        mapView.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    boolean endChanged = false;
    boolean startChanged = false;


    LatLng mLocation;

    Marker mMarker;
    private OnLocationChangedListener mListener;

    boolean firstLoc = true;

    @Override
    public void onLocationChanged(final AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                if (startChanged == false) {
                    startPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    LatLng start = new LatLng(startPoint.getLatitude(), startPoint.getLatitude());


                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(start);
                    markerOption.zIndex(-1);

                    markerOption.title("Depature");
                    departure= aMapLocation.getAddress();

                    aMap.addMarker(markerOption);
                }


                mListener.onLocationChanged(aMapLocation);
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                city = aMapLocation.getCity();
//                System.out.println("  8888888888888   " + aMapLocation.getCity());//城市信息

                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码


                mLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());


                double lat = aMapLocation.getLatitude();
                double lon = aMapLocation.getLongitude();
                Log.v("pcw", "lat : " + lat + " lon : " + lon);

                LatLng newLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());





            } else {

                Toasty.error(getActivity(), "Locating Error", Toast.LENGTH_SHORT, true).show();
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
//                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * 绘制两个坐标点之间的线段,从以前位置到现在位置
     */
    private void setUpMap(LatLng oldData, LatLng newData) {

        //  绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(Color.BLUE));

    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mLocationOption.setOnceLocation(false);
            /**
             * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
             * 注意：只有在高精度模式下的单次定位有效，其他方式无效
             */
            mLocationOption.setGpsFirst(true);
            // 设置发送定位请求的时间间隔,最小值为1000ms,1秒更新一次定位信息
            mLocationOption.setInterval(1000);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }


    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;


    }


}
