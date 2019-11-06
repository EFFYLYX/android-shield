package com.example.shield;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class EmergencyContactActivity extends AppCompatActivity {


    static String phoneno;
    private ViewPager mViewPager;


    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    private SupervisorRecyclerAdapter mAdapter;
    private String titles[] = new String[]{"Supervisors", "Supervisees"};
    private ViewPager viewPager;

    private SupervisorRecyclerAdapter mAdapter2;
//    static List<String> contactList = new ArrayList<>();

    ImageView imageView;

    static List<String> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);



        if ( getIntent().hasExtra("phone")){
            phoneno=getIntent().getStringExtra("phone");


        }

        if ( getIntent().hasExtra("user")){
//            phoneno=getIntent().getStringExtra("phone");
            imageView = findViewById(R.id.mHeader_iv);
            imageView.setImageDrawable(new ColorCircleDrawable(getIntent().getStringExtra("user")));


        }
        if ( getIntent().hasExtra("name")){
//            phoneno=getIntent().getStringExtra("phone");
            TextView tv = findViewById(R.id.name);
            tv.setText(getIntent().getStringExtra("name"));

        }

        Toolbar toolbar = findViewById(R.id.toobar_ec);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("lll");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        mTabLayout = (TabLayout) findViewById(R.id.tabs);


        list2 = new ArrayList<>();
        list2.add("2352552546346346");


//        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setAdapter(mSectionsPagerAdapter);


        mTabLayout.setupWithViewPager(mViewPager);



    }


    public static class PlaceholderFragment extends Fragment {
        @SuppressLint("HandlerLeak")
         Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:


                        ArrayList<User> temp = (ArrayList<User>) msg.obj;

                        if (temp == null ){
                            Toasty.error(getContext(), "No one helps you. ", Toast.LENGTH_SHORT, true).show();

                        }else {
                            //supervisorList = (List<User>) msg.obj;
                            for (User u : temp) {

                                if (u!=null) {

//                                    Log.d("shwo u ","======= u show\n"+ u.firstName);
                                    adapter.addItem(adapter.getItemCount(), u);
                                }else{
                                    Toasty.error(getContext(), "Error ", Toast.LENGTH_SHORT, true).show();
                                }
//                            supervisorList.add(u);
                            }
                        }
                            Log.d("supervisorList size", String.valueOf(supervisorList.size()));


                        break;
                    case 2:


                        ArrayList<User> tempsee = (ArrayList<User>) msg.obj;

                        if (tempsee == null){
                            Toasty.error(getContext(), "You help no one.", Toast.LENGTH_SHORT, true).show();

                        }else {
                            //supervisorList = (List<User>) msg.obj;
                            for (User u : tempsee) {

                                if (u!=null) {

                                    Log.d("shwo u ","======= u show\n"+ u.firstName);
                                    superviseeRecyclerAdapter.addItem(superviseeRecyclerAdapter.getItemCount(), u);
                                }else{
                                    Toasty.error(getContext(), "Error ", Toast.LENGTH_SHORT, true).show();
                                }
//                            supervisorList.add(u);
                            }
                        }
                        Log.d("superviseeList size", String.valueOf(superviseeList.size()));

                        break;

                }
            }
        };



        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        static List<User> supervisorList = new ArrayList<>();

        static List<User> superviseeList = new ArrayList<>();
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {


            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            System.out.println("~~~~~~~~~~~~ " + sectionNumber);
            fragment.setArguments(args);


            if (sectionNumber == 1) {






                supervisorList.clear();


            }

            if (sectionNumber==2){
                superviseeList.clear();
            }


            return fragment;
        }

        SupervisorRecyclerAdapter adapter;
        SuperviseeRecyclerAdapter superviseeRecyclerAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_tab, container, false);

            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);


            List<String> datalist = new ArrayList<>();

            LinearLayout addLayout = rootView.findViewById(R.id.add_contact);


            if (sectionNumber == 1) {

                System.out.println("the size supervisorlist," + supervisorList.size());
//                SupervisorRecyclerAdapter adapter = new SupervisorRecyclerAdapter(inflater.getContext(), supervisorList);
                adapter = new SupervisorRecyclerAdapter(phoneno,inflater.getContext(), supervisorList);
                recyclerView.setAdapter(adapter);


                addLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AddSupervisorActivity.class);
                        intent.putExtra("phone",phoneno);

                        startActivityForResult(intent, Activity.RESULT_FIRST_USER);


                    }
                });


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<User> emergency_contatcts = request.ShowEmergency(phoneno);


//                        Log.d("!!!!!!!!888888888888888888888888!!1", String.valueOf(emergency_contatcts.size()));
                        Message message = new Message();
                        message.what=1;
                        message.obj=emergency_contatcts;
                        mHandler.sendMessage(message);


                    }
                }).start();


            }

            if (sectionNumber == 2) {

                superviseeRecyclerAdapter = new SuperviseeRecyclerAdapter(inflater.getContext(), superviseeList);
//                adapter = new SuperviseeRecyclerAdapter(inflater.getContext(),datalist);
                recyclerView.setAdapter(superviseeRecyclerAdapter);

                addLayout.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<User> emergency_contatcts = request.ShowSupervisee(phoneno);
//                        Log.d("!!!!!888supervisee8!!1", String.valueOf(emergency_contatcts.size()));
                        Message message = new Message();
                        message.what=2;
                        message.obj=emergency_contatcts;
                        mHandler.sendMessage(message);


                    }
                }).start();
//                datalist.clear();
//                datalist.addAll(list2);
            }


            RecyclerView.LayoutManager lm = new LinearLayoutManager(inflater.getContext());


            recyclerView.setLayoutManager(lm);


            return rootView;

        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == 111) {



                Bundle bundle = data.getExtras();

                if(bundle.get("add") != null){
                    String[] s = bundle.getStringArray("add");
                    User u = new User(s[0],s[1],s[2]);

                adapter.addItem(supervisorList.size(), u);
                }





//                adapter.addItem(supervisorList.size(), bundle.getString("note"));


//                Log.d("GetFrom ASActivity", bundle.getString("note"));


            }
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;

        public SectionsPagerAdapter(Context mContext, FragmentManager fm) {
            super(fm);
            mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Who Helps You";
                case 1:
                    return "Who You Help";

                default:
                    return null;
            }
        }
    }
}
