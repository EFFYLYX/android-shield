package com.example.shield;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import es.dmoral.toasty.Toasty;
import q.rorbin.badgeview.QBadgeView;

public class TabActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ActionBarDrawerToggle mToggle;


    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    User mUser;
    public static String registrationID;
    Set<String> tagSet=new HashSet<>();

    private void setJPush(String alias){
//
//
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
//



//        tagSet.add("158");

        JPushInterface.setTags(this,0,tagSet);


        registrationID = JPushInterface.getRegistrationID(this);
        Log.d("TAG", "接收Registration Id : " + registrationID);

//        JPushMessage j = new JPushMessage();
//        j.

        //给极光推送设置标签和别名
//        JPushInterface.setAliasAndTags(this, alias, tagSet, tagAliasCallback);
    }


//    private final TagAliasCallback tagAliasCallback = new TagAliasCallback() {
//        @Override
//        public void gotResult(int code, String alias, Set<String> tagSet) {
//            switch (code) {
//                case 0:
//                    Log.i("TAG", "设置别名成功");
//                    break;
//                default:
//                    Log.i("TAG", "设置别名失败");
//                    break;
//            }
//        }
//    };

    String[] user_arr;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 0:


                    ArrayList<User> tempsee = (ArrayList<User>) msg.obj;

                    if (tempsee == null){
//                        Toasty.error(getApplicationContext(), "Please add your emergency contacts!", Toast.LENGTH_SHORT, true).show();

                    }else {
                        //supervisorList = (List<User>) msg.obj;

                        System.out.println("============ set tag");
                        for (User u : tempsee) {

                            if (u!=null) {



                                System.out.println(u.getPhoneNo());

                                tagSet.add(u.getPhoneNo());

//                                Log.d("shwo u ","======= u show\n"+ u.firstName);
//                                superviseeRecyclerAdapter.addItem(superviseeRecyclerAdapter.getItemCount(), u);
                            }else{
                                Toasty.error(getApplicationContext(), "Waiting, Loading", Toast.LENGTH_SHORT, true).show();
                            }
//                            supervisorList.add(u);
                        }

                        setJPush("Hi");

                        System.out.println("============ set tag");
                    }
//                    Log.d("superviseeList size", String.valueOf(superviseeList.size()));

                    break;

                case 3:

                    String s = (String) msg.obj;




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


                   Toasty.info(getApplicationContext(),s, Toast.LENGTH_SHORT, true).show();

                    break;


            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);




//        mUser = new User("18013523801","David","Beckham","1234");

        if ( getIntent().hasExtra("thisUser")){
            user_arr = getIntent().getStringArrayExtra("thisUser");

            Log.d("user_arr",user_arr[0]);

            mUser = new User(user_arr[0],user_arr[1],user_arr[2],user_arr[3]);


            JPushInterface.setAlias(this,0,mUser.getPhoneNo());

            if (getIntent().hasExtra("content")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Message message = new Message();
                        message.what = 3;
                        message.obj = getIntent().getStringExtra("content");

                        System.out.println("========my receiver check\n" + getIntent().getStringExtra("content"));
                        mHandler.sendMessage(message);
                    }
                }).start();
            }
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                ArrayList<User> emergency_contatcts = request.ShowSupervisee(mUser.phoneNo);
//                Log.d("!!!!!888supervisee8!!1", String.valueOf(emergency_contatcts.size()));
                Message message = new Message();
                message.what=0;
                message.obj=emergency_contatcts;
                mHandler.sendMessage(message);



            }
        }).start();


        String username = mUser.getUsername();

        Log.e("muser",username);
        String firstSymbol = mUser.getFirstSymbol();








//        JMessageClient.setDebugMode(true);
//        JMessageClient.init(this);
//
//





        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);



        toolbar.setNavigationIcon(R.drawable.ic_menu);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("S.H.I.E.L.D");

        mDrawerLayout = findViewById(R.id.drawerlayout);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        mNavigationView=findViewById(R.id.navi_view);

        mNavigationView.setNavigationItemSelectedListener(this);



        mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        View headerView = mNavigationView.getHeaderView(0);


        ImageView mHeader_iv = headerView.findViewById(R.id.mHeader_iv);
        TextView tv_username = headerView.findViewById(R.id.tv_username);


        tv_username.setText(username);

        mHeader_iv.setImageDrawable(new ColorCircleDrawable(firstSymbol));



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);



        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("com.dessert.mojito.CHANGE_STATUS")) {


                    Message message = new Message();
                    message.what =3;
                    message.obj=intent.getStringExtra("content");

                    System.out.println("========my receiver check\n"+ intent.getStringExtra("content"));
                    mHandler.sendMessage(message);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dessert.mojito.CHANGE_STATUS");
        registerReceiver(mReceiver, filter);

    }




//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.chat) {
////
////            startActivity(new Intent(TabActivity.this,NotificationActivity.class));
////
////            View view = findViewById(R.id.chat);
////
////            new QBadgeView(getApplicationContext()).bindTarget(view).setBadgeNumber(5);
////            return true;
////        }
//        return super.onOptionsItemSelected(item);
//
//    }

    public int notReadMsg(List<MyMessage> msgList){
        int number = 0;
        for (MyMessage msg: msgList){
            if(!msg.hasRead){
                number++;
            }
        }


        return number;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
//        getMenuInflater().inflate(R.menu.menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setCheckable (true);
                        int id = item.getItemId();
        System.out.println("e contact");
//                if (item.isChecked()){
//                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                    return false;
//                }
                switch (id){
                    case R.id.emergency_contact :
                        System.out.println("emegency contact");
                        Intent intent = new Intent(TabActivity.this,EmergencyContactActivity.class);

                        intent.putExtra("phone",mUser.getPhoneNo());
                        intent.putExtra("user",mUser.getFirstSymbol());
                        intent.putExtra("name",mUser.getUsername());
                        startActivity(intent);
                        break;

                    case R.id.logout:


                        Intent intent1 = new Intent(TabActivity.this,LoginActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent1);

//                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
//                System.out.println("e contact");
                mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;

        public SectionsPagerAdapter(Context mContext, FragmentManager fm) {
            super(fm);
            mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SupervisorFragment();
            }
            if (position ==1){
                return new SuperviseeFragment();
            }

            return new SupervisorFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Supervisor";
                case 1:
                    return "Supervisee";

                default:
                    return null;
            }
        }
    }
}
