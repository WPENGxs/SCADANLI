package com.example.scadanli;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Data extends SQLiteOpenHelper {
    public Data(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    /*
     * 建立SQLite数据库
     * */
    @Override
    public void onCreate(SQLiteDatabase Database) {
        String sql="CREATE TABLE user(userid integer primary key autoincrement,"+
                "title text not null,"+//标题
                "room text not null,"+//房间
                "可视化界面 text ,"+
                "信息 text,"+
                "控制行1 text,"+
                "控制行2 text"+
                ")";
        Database.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase Database, int oldVersion, int newVersion) {
    }
}
