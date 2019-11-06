package com.example.shield;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
//    SwipeMenuRecyclerView recyclerView;
    MsgRecylerAdapter msgRecylerAdapter;
    List<MyMessage> dataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Notification");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        dataList.add(new MyMessage("18013523801","System","System MyMessage","Welcome!"));
        dataList.add(new MyMessage("18013523801","System","Confirm","Please confirm the request."));

        msgRecylerAdapter = new MsgRecylerAdapter(getApplicationContext(),dataList);







      LinearLayoutManager lm = new LinearLayoutManager(this);

        recyclerView.setAdapter(msgRecylerAdapter);
        recyclerView.setLayoutManager(lm);






    }
}
