package com.example.nowledge.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;
import com.example.nowledge.UserActivity;

import java.util.ArrayList;
import java.util.List;


public class UtilHistory {
    private HisDBHelper du;
    private SQLiteDatabase db;
    private Context context;

    public UtilHistory(Context context){
        du = new HisDBHelper(context);
        db = du.getWritableDatabase();
        this.context = context;
    }

    private final String tableHis = du.TABLE_HIS;


    /**
     * 添加数据
     * */

    public void addHistory(String name, String course){
        String[] args = {name, course};
        Cursor cursor = db.rawQuery("select * from " + tableHis
                +"  where name=? and course=?", args);
        if (cursor.moveToNext()) {
            Log.d("sqlite history", "history already exists!");
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("course", course);
        db.insert(tableHis,null,contentValues);
        contentValues.clear();
        Log.d("sqlite3", "addHis to sql");
    }

    /**
     * 删除数据
     * */

    public void clearHistory() {
        db.execSQL("delete from " + tableHis);
    }


    /**
     * 查询数据
     * */

    public List<Pair<String, String>> getHisList() {
        Cursor cursor = db.rawQuery("select name,course from " + tableHis, null);
        List<Pair<String,String>> hisList = new ArrayList<>();
        while(cursor.moveToNext()) {
            String name = cursor.getString(0), course = cursor.getString(1);
            hisList.add(Pair.create(name, course));
        }
        return hisList;
    }

    /**
     * 关闭数据库连接
     * */
    public void getClose(){
        if(db != null){
            db.close();
        }
    }
}