package com.example.nowledge.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "entity.db";
    public static final String TABLE_NAME = "Entities";

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE_NAME
                + " (id integer primary key autoincrement, name text, course text,"
                + " properties text, subject_content text, object_content text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}

