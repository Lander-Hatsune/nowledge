package com.example.nowledge.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class HisDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "history.db";
    public static final String TABLE_HIS = "History";

    public HisDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql1 = "create table if not exists " + TABLE_HIS
                + " (id integer primary key autoincrement, name text, course text)";
        sqLiteDatabase.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE IF EXISTS " + TABLE_HIS;
        sqLiteDatabase.execSQL(sql1);
        onCreate(sqLiteDatabase);
    }
}

