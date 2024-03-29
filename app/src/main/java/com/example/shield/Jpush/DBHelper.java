package com.example.shield.Jpush;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by effy on 2018/11/16.
 */

public class DBHelper extends SQLiteOpenHelper {
    // 数据库名
    private static final String DATABASE_NAME = "finch.db";

    // 表名
//    public static final String USER_TABLE_NAME = "user";
//    public static final String JOB_TABLE_NAME = "job";


    public static final String USERLOCATION_TABLE_NAME = "userlocation";

    private static final int DATABASE_VERSION = 1;
    //数据库版本号

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USERLOCATION_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + " name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
