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
                "type text not null,"+//类型 cir,control,display
                "room text not null,"+//房间 sheds,greenhouses,fields
                "line text not null,"+//布局的行数
                "title text ,"+//名称
                "value text,"+//值 cir_bar 进度,control 开/关,display 数值
                "unit text,"+//单位 只有display有此项
                "remark text"+//留的冗余项
                ")";
        Database.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase Database, int oldVersion, int newVersion) {
    }
}
