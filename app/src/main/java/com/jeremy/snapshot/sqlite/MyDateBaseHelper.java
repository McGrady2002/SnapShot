/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：MyDateBaseHelper.java
 * 包名：com.jeremy.snapshot.sqlite.MyDateBaseHelper
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年10月23日 21:15:12
 */

package com.jeremy.snapshot.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDateBaseHelper extends SQLiteOpenHelper {

    public static final String CREAT_USER="create table user(" +
            "account varchar(20) primary key," +
            "password varchar(20))";

    public MyDateBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
