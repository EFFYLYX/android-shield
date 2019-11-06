package com.example.shield;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import es.dmoral.toasty.Toasty;

public class AddSupervisorActivity extends AppCompatActivity {
    private EditText et_name;
    private EditText et_phone;
    private TextView tv_contacts;
    private Button btn_confirm;


    private String username, usernumber;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:

                    User u = (User) msg.obj;
//                    System.out.println("----------------add handler \n"+u.firstName);
                    if (u == null){
                        Toasty.error(getApplicationContext(), "Not Exist", Toast.LENGTH_SHORT, true).show();

                    }else{
                        Bundle bundle = new Bundle();

                        String name = u.getUsername();
                        String[] s ={ u.phoneNo, u.firstName,u.secondName};
                        bundle.putStringArray("add",s);

//                        bundle.putParcelable("add",u);


//                        bundle.putString("note", "8");

                        setResult(111, getIntent().putExtras(bundle));



                        finish();
                    }

                    break;
            }
        }
    };

String phoneno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( getIntent().hasExtra("phone")){
            phoneno=getIntent().getStringExtra("phone");
//            user_arr = getIntent().getStringArrayExtra("thisUser");
//
//            Log.d("user_arr",user_arr[0]);
//
//            mUser = new User(user_arr[0],user_arr[1],user_arr[2],user_arr[3]);


        }
        setContentView(R.layout.activity_add_supervisor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Contact");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        tv_contacts = findViewById(R.id.tv_contacts);
        btn_confirm = findViewById(R.id.btn_confrm);

        tv_contacts.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTelClick(v);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();

                        User u = request.AddEmergency(phoneno,et_phone.getText().toString());

//                        System.out.println("----------------addsuepr\n"+u.firstName);
                        msg.obj=u;
                        msg.what=0;
                        handler.sendMessage(msg);
                    }
                }).start();







            }
        });





    }


    public void getTelClick(View v) {

        startActivityForResult(new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            // 获得DATA表中的名字
            username = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // 条件为联系人ID
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phone = reContentResolverol.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            while (phone.moveToNext()) {
                usernumber = phone
                        .getString(phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                et_name.setText(username);
                et_phone.setText(usernumber);

//                et_mobile.setText(usernumber + " (" + username + ")");
            }

        }
    }

}
