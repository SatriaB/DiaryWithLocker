package com.example.zafkiel.diarywithlocker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
/**
 * Created by Zafkiel on 11/17/2017.
 */

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "diary.db";
    private static final int DATABASE_VERSION = 1;
    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "create table diary(" +
                "dt datetime null, " +
                "title text null, " +
                "content text null);";
        Log.d("Diary", "onCreate: " + sql);
        db.execSQL(sql);
        String sql2 = "create table account(username text, password text, email text);";
        Log.d("Data karyawan", "onCreate: " + sql2);
        db.execSQL(sql2);
//        String sql3 = "INSERT INTO diary(dt,title,content)VALUES ('0000-00-00 00:00:00', 'title here','content here');";
//        db.execSQL(sql3);
//        String sql4 = "INSERT INTO account(username,password,email)VALUES ('test', 'test','zirconspear.sb@gmail.com');";
//        db.execSQL(sql4);
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
    {
        // TODO Auto-generated method stub
    }
}
